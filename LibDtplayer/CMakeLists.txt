#
#
#
cmake_minimum_required(VERSION 3.4.1)

include_directories(
        src/main/ccpp/include
        src/main/ccpp/dtandroid
        src/main/ccpp/include/dt
        src/main/ccpp/dt/dtutils
        src/main/ccpp/dt/dtstream
        src/main/ccpp/dt/dtdemux
        src/main/ccpp/dt/dtaudio
        src/main/ccpp/dt/dtvideo
        src/main/ccpp/dt/dtsub
        src/main/ccpp/dt/dtport
        src/main/ccpp/dt/dthost
        src/main/ccpp/dt/dtplayer
        src/main/ccpp/dt/tools)

add_library(
        dtplayer-0.1

        # Sets the library as a shared library.
        SHARED

        #dtutils
        src/main/ccpp/dt/dtutils/dt_log.c
        src/main/ccpp/dt/dtutils/dt_setting.c
        src/main/ccpp/dt/dtutils/dt_ini.c
        src/main/ccpp/dt/dtutils/dt_time.c
        src/main/ccpp/dt/dtutils/dt_event.c
        src/main/ccpp/dt/dtutils/dt_mem.c
        src/main/ccpp/dt/dtutils/dt_buffer.c
        src/main/ccpp/dt/dtutils/dt_queue.c
        src/main/ccpp/dt/dtutils/commander.c
        src/main/ccpp/dt/dtutils/dt_interrupt.c

        #dtstream
        src/main/ccpp/dt/dtstream/dtstream_api.c
        src/main/ccpp/dt/dtstream/dtstream.c
        src/main/ccpp/dt/dtstream/stream/stream_file.c
        # SRCS_COMMON-$(DT_STREAM_CURL) +=dtstream/stream/stream_curl.c
        src/main/ccpp/dt/dtstream/stream/stream_cache.c
        src/main/ccpp/dt/dtstream/stream/stream_ffmpeg.c

        #dtdemuxer
        src/main/ccpp/dt/dtdemux/dtdemuxer_api.c
        src/main/ccpp/dt/dtdemux/dtdemuxer.c
        src/main/ccpp/dt/dtdemux/demuxer/demuxer_aac.c
        src/main/ccpp/dt/dtdemux/demuxer/demuxer_ffmpeg.c

        # src/main/ccpp/dt/dtdemux/demuxer/demuxer_ts.c
        # src/main/ccpp/dt/dtdemux/demuxer/ts/cat.c
        # src/main/ccpp/dt/dtdemux/demuxer/ts/pat.c
        # src/main/ccpp/dt/dtdemux/demuxer/ts/pmt.c
        # src/main/ccpp/dt/dtdemux/demuxer/ts/packet.c
        # src/main/ccpp/dt/dtdemux/demuxer/ts/stream.c
        # src/main/ccpp/dt/dtdemux/demuxer/ts/table.c
        # src/main/ccpp/dt/dtdemux/demuxer/ts/pid.c
        # src/main/ccpp/dt/dtdemux/demuxer/ts/types.c

        #dtaudio
        src/main/ccpp/dt/dtaudio/dtaudio_api.c
        src/main/ccpp/dt/dtaudio/dtaudio.c
        src/main/ccpp/dt/dtaudio/dtaudio_decoder.c
        src/main/ccpp/dt/dtaudio/dtaudio_filter.c
        src/main/ccpp/dt/dtaudio/dtaudio_output.c
        #SRCS_COMMON-$(DT_FAAD)  += dtaudio/audio_decoder/ad_faad.c            # dec
        src/main/ccpp/dt/dtaudio/audio_decoder/ad_ffmpeg.c                       # dec
        #SRCS_COMMON-$(DT_ALSA) += dtaudio/audio_out/ao_alsa.c                 # out

        #dtvideo
        src/main/ccpp/dt/dtvideo/dtvideo_api.c
        src/main/ccpp/dt/dtvideo/dtvideo.c
        src/main/ccpp/dt/dtvideo/dtvideo_decoder.c
        src/main/ccpp/dt/dtvideo/dtvideo_filter.c
        src/main/ccpp/dt/dtvideo/dtvideo_output.c
        src/main/ccpp/dt/dtvideo/video_decoder/vd_ffmpeg.c   #dec
        src/main/ccpp/dt/dtvideo/video_filter/vf_ffmpeg.c    #filter
        src/main/ccpp/dt/dtvideo/video_out/vo_null.c         #default-render

        #dtsub
        src/main/ccpp/dt/dtsub/dtsub_api.c
        src/main/ccpp/dt/dtsub/dtsub.c
        src/main/ccpp/dt/dtsub/dtsub_parser.c
        src/main/ccpp/dt/dtsub/dtsub_decoder.c
        src/main/ccpp/dt/dtsub/dtsub_filter.c
        src/main/ccpp/dt/dtsub/dtsub_output.c
        src/main/ccpp/dt/dtsub/sub_parser/sp_ffmpeg.c
        src/main/ccpp/dt/dtsub/sub_decoder/sd_ffmpeg.c
        src/main/ccpp/dt/dtsub/sub_output/so_null.c

        #dtport
        src/main/ccpp/dt/dtport/dt_packet_queue.c
        src/main/ccpp/dt/dtport/dtport_api.c
        src/main/ccpp/dt/dtport/dtport.c

        #dthost
        src/main/ccpp/dt/dthost/dthost_api.c
        src/main/ccpp/dt/dthost/dthost.c

        #dtplayer
        src/main/ccpp/dt/dtplayer/dtplayer_api.c
        src/main/ccpp/dt/dtplayer/dtplayer.c
        src/main/ccpp/dt/dtplayer/dtplayer_host.c
        src/main/ccpp/dt/dtplayer/dtplayer_io.c
        src/main/ccpp/dt/dtplayer/dtplayer_magic.c
        src/main/ccpp/dt/dtplayer/dtplayer_update.c
        src/main/ccpp/dt/dtplayer/dtplayer_frame.c

        #tools
        #src/main/ccpp/dt/tools/ao_sdl.c
        #src/main/ccpp/dt/tools/ao_sdl2.c
        #src/main/ccpp/dt/tools/dt_player.c
        #src/main/ccpp/dt/tools/dt_probe.c
        #src/main/ccpp/dt/tools/gui_sdl.c
        #src/main/ccpp/dt/tools/so_sdl2.c
        #src/main/ccpp/dt/tools/version.c
        #src/main/ccpp/dt/tools/vo_sdl2.c

        # dtplayer JNI
        src/main/ccpp/dtandroid/plugin/plugin_ao_opensl.c
        src/main/ccpp/dtandroid/plugin/plugin_so_android.cpp
        src/main/ccpp/dtandroid/plugin/plugin_vo_surface.cpp
        src/main/ccpp/dtandroid/dttv_jni.cpp
        src/main/ccpp/dtandroid/dttv_jni_dtp.cpp
        src/main/ccpp/dtandroid/dttv_jni_plugin.cpp
        src/main/ccpp/dtandroid/dttv_jni_surface.cpp
        # src/main/ccpp/dtandroid/dttv_jni_utils.c
)

add_library(libavcodec SHARED IMPORTED)
set_target_properties(
        libavcodec
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavcodec.so)

add_library(libavfilter SHARED IMPORTED)
set_target_properties(
        libavfilter
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavfilter.so)

add_library(libavformat SHARED IMPORTED)
set_target_properties(
        libavformat
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavformat.so)

add_library(libavutil SHARED IMPORTED)
set_target_properties(
        libavutil
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavutil.so)

add_library(libswresample SHARED IMPORTED)
set_target_properties(
        libswresample
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libswresample.so)

add_library(libswscale SHARED IMPORTED)
set_target_properties(
        libswscale
        PROPERTIES
        IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libswscale.so)

#find_library( log-lib     log )
#find_library( android-lib android )
#find_library( m-lib       m )
#find_library( z-lib       z )

target_link_libraries(
        dtplayer-0.1
        log
        android
        m
        z
        GLESv2
        libavcodec
        libavfilter
        libavformat
        libavutil
        libswresample
        libswscale
)
