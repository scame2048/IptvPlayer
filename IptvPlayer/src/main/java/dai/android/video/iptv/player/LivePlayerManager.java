package dai.android.video.iptv.player;

import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import dai.android.video.iptv.run.WorkManager;
import dai.android.video.iptv.utility.Logger;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public final class LivePlayerManager implements IPlayMonitor {
    private static final String TAG = "LiveManager";

    private static LivePlayerManager sLivePlayerManager = null;

    public static LivePlayerManager get() {
        if (null == sLivePlayerManager) {
            synchronized (LivePlayerManager.class) {
                if (null == sLivePlayerManager) {
                    sLivePlayerManager = new LivePlayerManager();
                }
            }
        }
        return sLivePlayerManager;
    }

    /***********************************************************************************************
     * private filed
     **********************************************************************************************/
    private final Set<IPlayMonitor> mMonitors = new HashSet<>();
    private final ReentrantReadWriteLock mReadWriteLock = new ReentrantReadWriteLock();

    public static final int PLAYER_SYS = 0;
    public static final int PLAYER_IJK = 1;

    private AbstractMediaPlayer mMediaPlayer;
    private int mPlayType = PLAYER_SYS;

    public void add(@NonNull IPlayMonitor monitor) {
        try {
            mReadWriteLock.writeLock().lock();
            mMonitors.add(monitor);
        } finally {
            mReadWriteLock.writeLock().unlock();
        }
    }

    public void remove(@NonNull IPlayMonitor monitor) {
        try {
            mReadWriteLock.writeLock().lock();
            mMonitors.remove(monitor);
        } finally {
            mReadWriteLock.writeLock().unlock();
        }
    }

    public void createMedia() {
        createMedia(PLAYER_IJK);
    }

    public void createMedia(int type) {
        switch (type) {
            case PLAYER_SYS:
            case PLAYER_IJK: {
                mPlayType = type;
                break;
            }
            default: {
                mPlayType = PLAYER_SYS;
                break;
            }
        }
        makeMediaPlayer();
    }

    public void setDataSource(@NonNull String url) {
        try {
            mMediaPlayer.setDataSource(url);
        } catch (Exception e) {
            Logger.e(TAG, "set data source failed.", e);
        }
    }

    public void start() {
        Logger.v(TAG, "[start]");

        if (null != mMediaPlayer) {
            mMediaPlayer.start();
        }
    }

    public void stop() {
        Logger.v(TAG, "[stop]");

        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
        }
    }

    public void release() {
        Logger.v(TAG, "[release]");

        if (null != mMediaPlayer) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void reset() {
        Logger.v(TAG, "[reset]");

        if (null != mMediaPlayer) {
            mMediaPlayer.reset();
        }
    }

    public void pause() {
        Logger.v(TAG, "[pause]");

        if (null != mMediaPlayer) {
            mMediaPlayer.pause();
        }
    }

    public void prepareAsync() {
        Logger.v(TAG, "[prepareAsync]");

        if (null != mMediaPlayer) {
            mMediaPlayer.prepareAsync();
        }
    }

    public void setDisplay(SurfaceHolder holder) {
        Logger.v(TAG, "[setDisplay]");

        if (null != mMediaPlayer) {
            mMediaPlayer.setDisplay(holder);
        }
    }

    public boolean isPlaying() {
        if (null != mMediaPlayer) {
            mMediaPlayer.isPlaying();
        }
        return false;
    }


    /***********************************************************************************************
     * private function
     **********************************************************************************************/
    private void makeMediaPlayer() {
        if (mPlayType == PLAYER_IJK) {
            if (mMediaPlayer instanceof AndroidMediaPlayer) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();

            // 相关的配置设置 https://www.jianshu.com/p/843c86a9e9ad

            // 如下配置可以秒开
            // https://blog.csdn.net/u014614038/article/details/78350324

            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 0);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fps", 30);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_YV12);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);

            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 8);


            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 10240L);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0L);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1L);


            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);

            // 设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

            // 播放重连次数
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 10);

            // 设置是否开启变调
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1);

            // 设置播放前的最大探测时间
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);

            // 设置播放前的探测时间 1, 达到首屏秒开效果
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1);

            // 播放前的探测Size，默认是1M, 改小一点会出画面更快
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 4096L);

            // 每处理一个packet之后刷新io上下文
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);

            // 是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 1L); // 建议为 0L

            // 最大缓冲大小,单位kb
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 4096);

            // 跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5);

            // 最大fps
            ///ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fps", 30);

            // 某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容
            // 视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中 i 帧比较少
            // ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);


            // 另外一种配置方式
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 60);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 0);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fps", 30);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV16);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 1024);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 3);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probsize", "4096");
            /// ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", "2000000");

            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 60);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 0);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fps", 30);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_YV12);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 1024);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 3);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probsize", "4096");
            ///  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", "2000000");

            //硬解码：1、打开，0、关闭
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            //软解码：1、打开，0、关闭
            //mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "videotoolbox", 0);

            //rtsp设置 https://ffmpeg.org/ffmpeg-protocols.html#rtsp
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_flags", "prefer_tcp");
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "allowed_media_types", "video"); //根据媒体类型来配置
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 20000);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "buffer_size", 1316);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "infbuf", 1);  // 无限读
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 10240L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);
            //  关闭播放器缓冲，这个必须关闭，否则会出现播放一段时间后，一直卡主，控制台打印 FFP_MSG_BUFFERING_START
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0L);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1L);

            ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            ijkMediaPlayer.setVolume(1.0f, 1.0f);


            //
            mMediaPlayer = ijkMediaPlayer;
        }

        if (mPlayType == PLAYER_SYS) {
            if (mMediaPlayer instanceof IjkMediaPlayer) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mMediaPlayer = new AndroidMediaPlayer();
        }

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnTimedTextListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
    }

    private void dispatchMonitor(@NonNull IMonitorWrapper<IPlayMonitor> action) {
        // Logger.v(TAG, "[dispatchMonitor]");
        try {
            mReadWriteLock.readLock().lock();
            for (IPlayMonitor one : mMonitors) {
                if (null == one) {
                    continue;
                }
                try {
                    action.doAction(one);
                } catch (Exception e) {
                    Logger.w(TAG, "dispatch monitor failed:", e);
                }
            }
        } finally {
            mReadWriteLock.readLock().unlock();
        }
    }


    /***********************************************************************************************
     * implement interface of  IMonitorWrapper<IPlayMonitor>
     **********************************************************************************************/
    @Override
    public void onBufferingUpdate(final IMediaPlayer player, final int i) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatchMonitor(new IMonitorWrapper<IPlayMonitor>() {
                    @Override
                    public void doAction(IPlayMonitor action) {
                        action.onBufferingUpdate(player, i);
                    }
                });
            }
        });
    }

    @Override
    public void onCompletion(final IMediaPlayer player) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatchMonitor(new IMonitorWrapper<IPlayMonitor>() {
                    @Override
                    public void doAction(IPlayMonitor action) {
                        action.onCompletion(player);
                    }
                });
            }
        });
    }

    @Override
    public boolean onError(final IMediaPlayer player, final int i, final int i1) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatchMonitor(new IMonitorWrapper<IPlayMonitor>() {
                    @Override
                    public void doAction(IPlayMonitor action) {
                        action.onError(player, i, i1);
                    }
                });
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(final IMediaPlayer player, final int i, final int i1) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatchMonitor(new IMonitorWrapper<IPlayMonitor>() {
                    @Override
                    public void doAction(IPlayMonitor action) {
                        action.onInfo(player, i, i1);
                    }
                });
            }
        });
        return true;
    }

    @Override
    public void onPrepared(final IMediaPlayer player) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatchMonitor(new IMonitorWrapper<IPlayMonitor>() {
                    @Override
                    public void doAction(IPlayMonitor action) {
                        action.onPrepared(player);
                    }
                });
            }
        });
    }

    @Override
    public void onSeekComplete(final IMediaPlayer player) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatchMonitor(new IMonitorWrapper<IPlayMonitor>() {
                    @Override
                    public void doAction(IPlayMonitor action) {
                        action.onSeekComplete(player);
                    }
                });
            }
        });
    }

    @Override
    public void onTimedText(final IMediaPlayer player, final IjkTimedText ijkTimedText) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatchMonitor(new IMonitorWrapper<IPlayMonitor>() {
                    @Override
                    public void doAction(IPlayMonitor action) {
                        action.onTimedText(player, ijkTimedText);
                    }
                });
            }
        });
    }

    @Override
    public void onVideoSizeChanged(final IMediaPlayer player,
                                   final int i, final int i1, final int i2, final int i3) {
        execute(new Runnable() {
            @Override
            public void run() {
                dispatchMonitor(new IMonitorWrapper<IPlayMonitor>() {
                    @Override
                    public void doAction(IPlayMonitor action) {
                        action.onVideoSizeChanged(player, i, i1, i2, i3);
                    }
                });
            }
        });
    }

    /***********************************************************************************************
     * a static private function
     **********************************************************************************************/
    private static void execute(Runnable runnable) {
        WorkManager.get().execute(runnable);
    }
}
