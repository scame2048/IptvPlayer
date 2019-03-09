#!/bin/bash

PWD=`pwd`
DEST=$PWD/build/ffmpeg && rm -rf $DEST
SOURCE=$PWD/ffmpeg-4.0.3


export ANDROID_NDK=/home/patrick/tools/google/ndk/android-ndk-r16b

TOOLCHAIN_64=/tmp/NDK_64
SYSROOT_64=$TOOLCHAIN_64/sysroot/
$ANDROID_NDK/build/tools/make_standalone_toolchain.py --api=27 --arch=arm64 --force --install-dir=$TOOLCHAIN_64

#export PATH=$TOOLCHAIN_32/bin:$PATH
export PATH=$TOOLCHAIN_64/bin:$PATH

FF_CFG_FLAGS=
EXTRA_CFLAGS=
EXTRA_LDFLAGS=

CFLAGS="-O3 -Wall -pipe \
    -std=c99 \
    -ffast-math \
    -Werror=strict-aliasing \
    -Wno-psabi -Wa,--noexecstack \
    -DANDROID -DNDEBUG"

FFMPEG_FLAGS="
  --disable-debug \
  --enable-shared \
  --enable-pthreads \
  --disable-symver \
  --disable-doc \
  --disable-ffplay \
  --disable-ffmpeg \
  --disable-ffprobe \
  --disable-avdevice \
  --disable-encoders \
  --disable-muxers \
  --disable-devices \
  --disable-indevs \
  --disable-outdevs \
  --enable-protocols  \
  --enable-parsers \
  --disable-demuxers \
  --disable-decoders \
  --enable-bsfs \
  --enable-jni \
  --enable-mediacodec \
  --enable-network \
  --enable-swscale  \
  --disable-asm \
  --disable-linux-perf \
  --enable-demuxer=aac \
  --enable-demuxer=ac3 \
  --enable-demuxer=amr \
  --enable-demuxer=ape \
  --enable-demuxer=asf \
  --enable-demuxer=avs \
  --enable-demuxer=cavsvideo \
  --enable-demuxer=dts \
  --enable-demuxer=dvbsub \
  --enable-demuxer=dvbtxt \
  --enable-demuxer=eac3 \
  --enable-demuxer=flac \
  --enable-demuxer=flv \
  --enable-demuxer=g722 \
  --enable-demuxer=g729 \
  --enable-demuxer=h261 \
  --enable-demuxer=h263 \
  --enable-demuxer=h264 \
  --enable-demuxer=hevc \
  --enable-demuxer=hls \
  --enable-demuxer=m4v \
  --enable-demuxer=matroska \
  --enable-demuxer=mjpeg \
  --enable-demuxer=mjpeg_2000 \
  --enable-demuxer=mov \
  --enable-demuxer=mp3 \
  --enable-demuxer=mpegps \
  --enable-demuxer=mpegts \
  --enable-demuxer=mpegtsraw \
  --enable-demuxer=mpegvideo \
  --enable-demuxer=mpjpeg \
  --enable-demuxer=pcm_alaw \
  --enable-demuxer=pcm_f32be \
  --enable-demuxer=pcm_f32le \
  --enable-demuxer=pcm_f64be \
  --enable-demuxer=pcm_f64le \
  --enable-demuxer=pcm_mulaw \
  --enable-demuxer=pcm_s16be \
  --enable-demuxer=pcm_s16le \
  --enable-demuxer=pcm_s24be \
  --enable-demuxer=pcm_s24le \
  --enable-demuxer=pcm_s32be \
  --enable-demuxer=pcm_s32le \
  --enable-demuxer=pcm_s8 \
  --enable-demuxer=pcm_u16be \
  --enable-demuxer=pcm_u16le \
  --enable-demuxer=pcm_u24be \
  --enable-demuxer=pcm_u24le \
  --enable-demuxer=pcm_u32be \
  --enable-demuxer=pcm_u32le \
  --enable-demuxer=pcm_u8 \
  --enable-demuxer=rawvideo \
  --enable-demuxer=rm \
  --enable-demuxer=rtp \
  --enable-demuxer=rtsp \
  --enable-demuxer=vc1 \
  --enable-demuxer=vobsub \
  --enable-demuxer=wav \
  --enable-demuxer=webvtt \
  --enable-decoder=aac \
  --enable-decoder=aac_latm \
  --enable-decoder=ac3 \
  --enable-decoder=alac \
  --enable-decoder=amrnb \
  --enable-decoder=amrwb \
  --enable-decoder=ass \
  --enable-decoder=truehd \
  --enable-decoder=cavs \
  --enable-decoder=dnx \
  --enable-decoder=dnxhd \
  --enable-decoder=cook \
  --enable-decoder=dvbsub \
  --enable-decoder=dvdsub \
  --enable-decoder=eac3 \
  --enable-decoder=vc1 \
  --enable-decoder=flac \
  --enable-decoder=flv \
  --enable-decoder=g729 \
  --enable-decoder=h261 \
  --enable-decoder=h263 \
  --enable-decoder=h264 \
  --enable-decoder=hevc \
  --enable-decoder=vp8 \
  --enable-decoder=vp8_mediacodec \
  --enable-decoder=h264_mediacodec \
  --enable-decoder=hevc_mediacodec \
  --enable-decoder=mjpeg \
  --enable-decoder=mp2 \
  --enable-decoder=mp3 \
  --enable-decoder=mpeg2video \
  --enable-decoder=mpeg4 \
  --enable-decoder=mpegvideo \
  --enable-decoder=vp9 \
  --enable-decoder=vp9_mediacodec \
  --enable-decoder=webvtt \
  --enable-decoder=wmalossless \
  --enable-decoder=wmapro \
  --enable-decoder=pgssub \
  --enable-decoder=rv10 \
  --enable-decoder=rv20 \
  --enable-decoder=rv30 \
  --enable-decoder=rv40 \
  --enable-decoder=yuv4 \
  --enable-version3"


#for version in neon armv7 vfp armv6 armv8; do
#for version in armv7 armv8; do
for version in armv8; do

  cd $SOURCE
  make clean
  make distclean

  case $version in
    armv7)
      FF_CFG_FLAGS="--arch=arm --cpu=cortex-a8"
      FF_CFG_FLAGS="$FF_CFG_FLAGS --enable-neon"
      FF_CFG_FLAGS="$FF_CFG_FLAGS --enable-thumb"
      FF_CROSS_PREFIX=arm-linux-androideabi
      
      EXTRA_CFLAGS="-march=armv7-a -mcpu=cortex-a8 -mfpu=vfpv3-d16 -mfloat-abi=softfp -mthumb"
      EXTRA_LDFLAGS="-Wl,--fix-cortex-a8"
      ;;
    armv8)
      FF_CFG_FLAGS="--arch=aarch64"
      FF_CROSS_PREFIX=aarch64-linux-android
      
      EXTRA_CFLAGS=""
      EXTRA_LDFLAGS="$FF_EXTRA_LDFLAGS"
      ;;
    *)
      EXTRA_CFLAGS=""
      EXTRA_LDFLAGS=""
      ;;
  esac

  PREFIX="$DEST/$version" && mkdir -p $PREFIX
  FF_CFG_FLAGS="$FF_CFG_FLAGS $FFMPEG_FLAGS";
  FF_CFG_FLAGS="$FF_CFG_FLAGS --prefix=$PREFIX"
  FF_CFG_FLAGS="$FF_CFG_FLAGS --cross-prefix=${FF_CROSS_PREFIX}-"
  FF_CFG_FLAGS="$FF_CFG_FLAGS --enable-cross-compile"
  FF_CFG_FLAGS="$FF_CFG_FLAGS --target-os=android"


  export CC="${FF_CROSS_PREFIX}-gcc"
  export LD="${FF_CROSS_PREFIX}-ld"
  export AR="${FF_CROSS_PREFIX}-ar"
  export STRIP="{$FF_CROSS_PREFIX}-strip"

  ./configure $FF_CFG_FLAGS --extra-cflags="$CFLAGS $EXTRA_CFLAGS" --extra-ldflags="$EXTRA_LDFLAGS" | tee $PREFIX/configuration.txt
  cp config.* $PREFIX
  [ $PIPESTATUS == 0 ] || exit 1

  make -j32 || exit 1
  make install || exit 1

  $AR rcs $PREFIX/libffmpeg.a libavutil/*.o libavcodec/*.o libavformat/*.o libswresample/*.o libswscale/*.o  
  arm-linux-androideabi-strip --strip-unneeded $PREFIX/libffmpeg.a

done