package dai.android.video.iptv.player;

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
            mMediaPlayer = new IjkMediaPlayer();
        }

        if (mPlayType == PLAYER_SYS) {
            if (mMediaPlayer instanceof IjkMediaPlayer) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mMediaPlayer = new AndroidMediaPlayer();
        }

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
