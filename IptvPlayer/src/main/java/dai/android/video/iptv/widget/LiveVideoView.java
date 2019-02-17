package dai.android.video.iptv.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import dai.android.video.iptv.player.AbstractPlayMonitor;
import dai.android.video.iptv.player.LivePlayerManager;
import dai.android.video.iptv.run.WorkManager;
import dai.android.video.iptv.utility.Logger;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class LiveVideoView extends FrameLayout {
    private static final String TAG = "LiveVideo";

    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARED = 1;
    private static final int STATE_BUFFER = 2;

    private final SurfaceView mSurfaceView;
    private final LivePlayerManager mPlayer;

    private int mState = STATE_IDLE;

    public LiveVideoView(@NonNull Context context) {
        this(context, null);
    }

    public LiveVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPlayer = LivePlayerManager.get();

        mSurfaceView = new SurfaceView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mSurfaceView, 0, params);
        bindHolder();

        execute(new Runnable() {
            @Override
            public void run() {
                // 绑定回调函数
                mPlayer.add(mPlayerMonitor);
            }
        });
    }

    public void setDataSource(String url) {
        mPlayer.setDataSource(url);

        // prepare
        prepareAsync();
    }

    public void stop() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    public void start() {
        if (mPlayer.isPlaying()) {
            return;
        }
        mPlayer.start();
    }

    public void pause() {
        mPlayer.pause();
    }

    public void release() {
        mPlayer.release();
    }

    public void prepareAsync() {
        mPlayer.prepareAsync();
    }

    private void bindHolder() {
        mSurfaceView.getHolder().addCallback(mCallBack);
    }

    private AbstractPlayMonitor mPlayerMonitor = new AbstractPlayMonitor() {
        @Override
        public void onPrepared(IMediaPlayer player) {
            Logger.d(TAG, "prepared");

            // mState = STATE_PREPARED;

            // 如果 prepared 则开始播放
            mPlayer.start();
        }
    };

    private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Logger.d(TAG, "surface created");

            // holder 创建好
            mPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    /***********************************************************************************************
     * a static private function
     **********************************************************************************************/
    private static void execute(Runnable runnable) {
        WorkManager.get().execute(runnable);
    }
}
