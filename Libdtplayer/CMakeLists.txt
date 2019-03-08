#
#
#
cmake_minimum_required(VERSION 3.4.1)

include_directories(
        src/main/ccpp/include
        src/main/ccpp/dtutils
        src/main/ccpp/dtstream
        src/main/ccpp/dtdemux
        src/main/ccpp/dtaudio
        src/main/ccpp/dtvideo
        src/main/ccpp/dtsub
        src/main/ccpp/dtport
        src/main/ccpp/dthost
        src/main/ccpp/dtplayer
        src/main/ccpp/tools)

add_library(
        dtplayer-0.1

        # Sets the library as a shared library.
        SHARED

        #dtutils
        src/main/ccpp/dtutils/dt_log.c
        src/main/ccpp/dtutils/dt_setting.c
        src/main/ccpp/dtutils/dt_ini.c
        src/main/ccpp/dtutils/dt_time.c
        src/main/ccpp/dtutils/dt_event.c
        src/main/ccpp/dtutils/dt_mem.c
        src/main/ccpp/dtutils/dt_buffer.c
        src/main/ccpp/dtutils/dt_queue.c
        src/main/ccpp/dtutils/commander.c
        src/main/ccpp/dtutils/dt_interrupt.c

        #dtstream
        src/main/ccpp/dtstream/dtstream_api.c
        src/main/ccpp/dtstream/dtstream.c
        src/main/ccpp/dtstream/stream/stream_file.c
        # SRCS_COMMON-$(DT_STREAM_CURL) +=dtstream/stream/stream_curl.c
        src/main/ccpp/dtstream/stream/stream_cache.c
        src/main/ccpp/dtstream/stream/stream_ffmpeg.c

        #dtdemuxer
        src/main/ccpp/dtdemux/dtdemuxer_api.c
        src/main/ccpp/dtdemux/dtdemuxer.c
        src/main/ccpp/dtdemux/demuxer/demuxer_aac.c
        src/main/ccpp/dtdemux/demuxer/demuxer_ffmpeg.c

        # src/main/ccpp/dtdemux/demuxer/demuxer_ts.c
        # src/main/ccpp/dtdemux/demuxer/ts/cat.c
        # src/main/ccpp/dtdemux/demuxer/ts/pat.c
        # src/main/ccpp/dtdemux/demuxer/ts/pmt.c
        # src/main/ccpp/dtdemux/demuxer/ts/packet.c
        # src/main/ccpp/dtdemux/demuxer/ts/stream.c
        # src/main/ccpp/dtdemux/demuxer/ts/table.c
        # src/main/ccpp/dtdemux/demuxer/ts/pid.c
        # src/main/ccpp/dtdemux/demuxer/ts/types.c

        #dtaudio
        src/main/ccpp/dtaudio/dtaudio_api.c
        src/main/ccpp/dtaudio/dtaudio.c
        src/main/ccpp/dtaudio/dtaudio_decoder.c
        src/main/ccpp/dtaudio/dtaudio_filter.c
        src/main/ccpp/dtaudio/dtaudio_output.c
        #SRCS_COMMON-$(DT_FAAD)  += dtaudio/audio_decoder/ad_faad.c            # dec
        src/main/ccpp/dtaudio/audio_decoder/ad_ffmpeg.c                       # dec
        #SRCS_COMMON-$(DT_ALSA) += dtaudio/audio_out/ao_alsa.c                 # out

        #dtvideo
        src/main/ccpp/dtvideo/dtvideo_api.c
        src/main/ccpp/dtvideo/dtvideo.c
        src/main/ccpp/dtvideo/dtvideo_decoder.c
        src/main/ccpp/dtvideo/dtvideo_filter.c
        src/main/ccpp/dtvideo/dtvideo_output.c
        src/main/ccpp/dtvideo/video_decoder/vd_ffmpeg.c   #dec
        src/main/ccpp/dtvideo/video_filter/vf_ffmpeg.c    #filter
        src/main/ccpp/dtvideo/video_out/vo_null.c         #default-render

        #dtsub
        src/main/ccpp/dtsub/dtsub_api.c
        src/main/ccpp/dtsub/dtsub.c
        src/main/ccpp/dtsub/dtsub_parser.c
        src/main/ccpp/dtsub/dtsub_decoder.c
        src/main/ccpp/dtsub/dtsub_filter.c
        src/main/ccpp/dtsub/dtsub_output.c
        src/main/ccpp/dtsub/sub_parser/sp_ffmpeg.c
        src/main/ccpp/dtsub/sub_decoder/sd_ffmpeg.c
        src/main/ccpp/dtsub/sub_output/so_null.c

        #dtport
        src/main/ccpp/dtport/dt_packet_queue.c
        src/main/ccpp/dtport/dtport_api.c
        src/main/ccpp/dtport/dtport.c

        #dthost
        src/main/ccpp/dthost/dthost_api.c
        src/main/ccpp/dthost/dthost.c

        #dtplayer
        src/main/ccpp/dtplayer/dtplayer_api.c
        src/main/ccpp/dtplayer/dtplayer.c
        src/main/ccpp/dtplayer/dtplayer_host.c
        src/main/ccpp/dtplayer/dtplayer_io.c
        src/main/ccpp/dtplayer/dtplayer_magic.c
        src/main/ccpp/dtplayer/dtplayer_update.c
        src/main/ccpp/dtplayer/dtplayer_frame.c

        #tools
        #src/main/ccpp/tools/ao_sdl.c
        #src/main/ccpp/tools/ao_sdl2.c
        #src/main/ccpp/tools/dt_player.c
        #src/main/ccpp/tools/dt_probe.c
        #src/main/ccpp/tools/gui_sdl.c
        #src/main/ccpp/tools/so_sdl2.c
        #src/main/ccpp/tools/version.c
        #src/main/ccpp/tools/vo_sdl2.c
)

add_library( libavcodec SHARED IMPORTED )
set_target_properties(
        libavcodec
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavcodec.so )

add_library( libavfilter SHARED IMPORTED )
set_target_properties(
        libavfilter
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavfilter.so )

add_library( libavformat SHARED IMPORTED )
set_target_properties(
        libavformat
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavformat.so )

add_library( libavutil SHARED IMPORTED )
set_target_properties(
        libavutil
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavutil.so )

add_library( libswresample SHARED IMPORTED )
set_target_properties(
        libswresample
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libswresample.so )

add_library( libswscale SHARED IMPORTED )
set_target_properties(
        libswscale
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libswscale.so )

find_library(
        log-lib
        log
)

find_library(
        android-lib
        android
)

find_library(
        m-lib
        m
)

find_library(
        z-lib
        z
)


target_link_libraries(
        dtplayer-0.1
        ${log-lib}
        ${android-lib}
        ${m-lib}
        ${z-lib}
        libavcodec
        libavfilter
        libavformat
        libavutil
        libswresample
        libswscale
)
