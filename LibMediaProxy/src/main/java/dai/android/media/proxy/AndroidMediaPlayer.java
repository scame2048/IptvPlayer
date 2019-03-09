package dai.android.media.proxy;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

public class AndroidMediaPlayer extends AbstractMediaPlayer {
    private final Object mInitLock = new Object();

    private final MediaPlayer mInternalMediaPlayer;
    private final AndroidMediaPlayerListenerHolder mInternalListenerAdapter;

    private boolean mIsReleased;

    public AndroidMediaPlayer() {
        synchronized (mInitLock) {
            mInternalMediaPlayer = new MediaPlayer();
        }
        mInternalMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mInternalListenerAdapter = new AndroidMediaPlayerListenerHolder(this);
        attachInternalListeners();
    }

    private void attachInternalListeners() {
        mInternalMediaPlayer.setOnPreparedListener(mInternalListenerAdapter);
        mInternalMediaPlayer
                .setOnBufferingUpdateListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnCompletionListener(mInternalListenerAdapter);
        mInternalMediaPlayer
                .setOnSeekCompleteListener(mInternalListenerAdapter);
        mInternalMediaPlayer
                .setOnVideoSizeChangedListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnErrorListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnInfoListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnTimedTextListener(mInternalListenerAdapter);
    }

    public MediaPlayer systemMediaPlayer() {
        return mInternalMediaPlayer;
    }


    @Override
    public void setDisplay(SurfaceHolder sh) {
        synchronized (mInitLock) {
            if (!mIsReleased) {
                mInternalMediaPlayer.setDisplay(sh);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setSurface(Surface surface) {
        mInternalMediaPlayer.setSurface(surface);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setDataSource(Context context, Uri uri) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mInternalMediaPlayer.setDataSource(context, uri);
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mInternalMediaPlayer.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd) throws
            IOException, IllegalArgumentException, IllegalStateException {
        mInternalMediaPlayer.setDataSource(fd);
    }

    @Override
    public void setDataSource(String path) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        Uri uri = Uri.parse(path);
        String scheme = uri.getScheme();
        if (!TextUtils.isEmpty(scheme) && scheme.equalsIgnoreCase("file")) {
            mInternalMediaPlayer.setDataSource(uri.getPath());
        } else {
            mInternalMediaPlayer.setDataSource(path);
        }
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        mInternalMediaPlayer.prepareAsync();
        notifyOnPrepare();
    }

    @Override
    public void start() throws IllegalStateException {
        mInternalMediaPlayer.start();
        notifyOnStart();
    }

    @Override
    public void stop() throws IllegalStateException {
        mInternalMediaPlayer.stop();
        notifyOnStop();
    }

    @Override
    public void pause() throws IllegalStateException {
        mInternalMediaPlayer.pause();
        notifyOnPause();
    }

    @Override
    public int getVideoWidth() {
        return mInternalMediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mInternalMediaPlayer.getVideoHeight();
    }

    @Override
    public boolean isPlaying() {
        try {
            return mInternalMediaPlayer.isPlaying();
        } catch (IllegalStateException ignore) {
            return false;
        }
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        mInternalMediaPlayer.seekTo((int) msec);
        notifyOnSeek();
    }

    @Override
    public long getCurrentPosition() {
        try {
            return mInternalMediaPlayer.getCurrentPosition();
        } catch (Exception ignore) {
            return -1;
        }
    }

    @Override
    public long getDuration() {
        try {
            return mInternalMediaPlayer.getDuration();
        } catch (Exception ignore) {
            return -1;
        }
    }

    @Override
    public void release() {
        mIsReleased = true;
        mInternalMediaPlayer.release();
        resetListener();
        attachInternalListeners();

        removeHandle();
        notifyOnRelease();
    }

    @Override
    public void reset() {
        try {
            mInternalMediaPlayer.reset();
        } catch (Exception ignore) {
        }
        resetListener();
        attachInternalListeners();

        notifyOnReset();
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mInternalMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public int getAudioSessionId() {
        return mInternalMediaPlayer.getAudioSessionId();
    }

    private class AndroidMediaPlayerListenerHolder implements
            MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
            MediaPlayer.OnBufferingUpdateListener,
            MediaPlayer.OnSeekCompleteListener,
            MediaPlayer.OnVideoSizeChangedListener,
            MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
            MediaPlayer.OnTimedTextListener {

        public final WeakReference<AndroidMediaPlayer> mWeakMediaPlayer;

        public AndroidMediaPlayerListenerHolder(AndroidMediaPlayer mp) {
            mWeakMediaPlayer = new WeakReference<>(mp);
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self == null) {
                return;
            }
            notifyOnBufferingUpdate(percent);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self == null) {
                return;
            }
            notifyOnComplete();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            return self != null && notifyOnError(what, extra);
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            return self != null && notifyOnInfo(what, extra);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self == null) {
                return;
            }
            notifyOnPrepared();
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self == null) {
                return;
            }
            notifyOnSeekComplete();
        }

        @Override
        public void onTimedText(MediaPlayer mp, TimedText text) {
            // TODO
        }

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self == null) {
                return;
            }
            notifyOnVideoSizeChanged(width, height, 1, 1);
        }
    }
}
