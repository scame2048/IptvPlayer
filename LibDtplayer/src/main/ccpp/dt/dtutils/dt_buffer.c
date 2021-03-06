#include "dt_buffer.h"
#include "dt_log.h"

#include "stdlib.h"
#include "stdint.h"
#include "string.h"

#define TAG "DT-BUF"
/*buffer ops*/

int buf_init( dt_buffer_t * dbt, int size ) {
    uint8_t *buffer = ( uint8_t * ) malloc( size );
    if ( !buffer ) {
        dbt->data = NULL;
        return -1;
    }
    dbt->data = buffer;
    dbt->size = size;
    dbt->level = 0;
    dbt->rd_ptr = dbt->wr_ptr = dbt->data;
    dt_lock_init( &dbt->mutex, NULL );
    dt_info( TAG, "DTBUF INIT OK\n" );
    return 0;
}

int buf_reinit( dt_buffer_t * dbt ) {
    dt_lock( &dbt->mutex );
    dbt->level = 0;
    dbt->rd_ptr = dbt->wr_ptr = dbt->data;
    dt_unlock( &dbt->mutex );
    return 0;
}

int buf_release( dt_buffer_t * dbt ) {
    dt_lock( &dbt->mutex );
    if ( dbt->data ) {
        free( dbt->data );
    }
    dbt->size = 0;
    dt_unlock( &dbt->mutex );
    return 0;
}

static int buf_empty( dt_buffer_t * dbt ) {
    //no need to lock, will lock uplevel
    int ret = -1;
    if ( dbt->level == 0 ) {
        ret = 1;
    } else {
        ret = 0;
    }
    return ret;
}

static int buf_full( dt_buffer_t * dbt ) {
    //no need to lock, will lock uplevel
    int ret = -1;
    if ( dbt->level == dbt->size ) {
        ret = 1;
    } else {
        ret = 0;
    }
    return ret;
}

int buf_space( dt_buffer_t * dbt ) {
    int space = dbt->size - dbt->level;
    return space;
}

int buf_level( dt_buffer_t * dbt ) {
    int lev = dbt->level;
    return lev;
}

int buf_get( dt_buffer_t * dbt, uint8_t * out, int size ) {
    dt_lock( &dbt->mutex );
    int len = -1;
    len = buf_empty( dbt );
    if ( len == 1 ) {
        len = 0;
        goto QUIT;              //get nothing
    }

    len = MIN( dbt->level, size );
    if ( dbt->wr_ptr > dbt->rd_ptr ) {
        memcpy( out, dbt->rd_ptr, len );
        dbt->rd_ptr += len;
        dbt->level -= len;
        goto QUIT;

    } else if ( len <= ( int )( dbt->data + dbt->size - dbt->rd_ptr ) ) {
        memcpy( out, dbt->rd_ptr, len );
        dbt->rd_ptr += len;
        dbt->level -= len;
        goto QUIT;

    } else {
        int tail_len = ( int )( dbt->data + dbt->size - dbt->rd_ptr );
        memcpy( out, dbt->rd_ptr, tail_len );
        memcpy( out + tail_len, dbt->data, len - tail_len );
        dbt->rd_ptr = dbt->data + len - tail_len;
        dbt->level -= len;
        goto QUIT;
    }
QUIT:
    dt_unlock( &dbt->mutex );
    return len;
}

int buf_put( dt_buffer_t * dbt, uint8_t * in, int size ) {
    dt_lock( &dbt->mutex );
    int len = buf_full( dbt );
    if ( len == 1 ) {
        len = 0;
        goto QUIT;              // no space to write
    }

    len = MIN( dbt->size - dbt->level, size );
    if ( dbt->wr_ptr < dbt->rd_ptr ) {
        memcpy( dbt->wr_ptr, in, len );
        dbt->wr_ptr += len;
        dbt->level += len;
        goto QUIT;

    } else if ( len <= ( int )( dbt->data + dbt->size - dbt->wr_ptr ) ) {
        memcpy( dbt->wr_ptr, in, len );
        dbt->wr_ptr += len;
        dbt->level += len;
        goto QUIT;

    } else {
        int tail_len = ( int )( dbt->data + dbt->size - dbt->wr_ptr );
        memcpy( dbt->wr_ptr, in, tail_len );
        memcpy( dbt->data, in + tail_len, len - tail_len );
        dbt->wr_ptr = dbt->data + len - tail_len;
        dbt->level += len;
        goto QUIT;
    }
QUIT:
    dt_unlock( &dbt->mutex );
    return len;
}
