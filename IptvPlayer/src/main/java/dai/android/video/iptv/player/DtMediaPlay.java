package dai.android.video.iptv.player;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

import dai.android.media.dt.MediaPlayer;
import dai.android.media.proxy.AbstractMediaPlayer;

public class DtMediaPlay extends AbstractMediaPlayer {

    private final MediaPlayer mMediaPlayer;

    public DtMediaPlay(Context context) {
        mMediaPlayer = new MediaPlayer(context, false);
        mMediaPlayer.setOnCompletionListener(mOnCompletion);
        mMediaPlayer.setOnPreparedListener(mOnPrepared);
        mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdate);
        mMediaPlayer.setOnErrorListener(mOnError);
        mMediaPlayer.setOnInfoListener(mOnInfo);
        mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChanged);
        mMediaPlayer.setOnSeekCompleteListener(mOnSeekComplete);
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        mMediaPlayer.setDisplay(sh);
    }

    @Override
    public void setSurface(Surface surface) {
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public void setDataSource(Context context, Uri uri)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mMediaPlayer.setDataSource(context, uri);
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mMediaPlayer.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd)
            throws IOException, IllegalArgumentException, IllegalStateException {
        mMediaPlayer.setDataSource(fd);
    }

    @Override
    public void setDataSource(String path) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mMediaPlayer.setDataSource(path);
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        try {
            mMediaPlayer.prepareAsync();
            notifyOnPrepare();
        } catch (Exception e) {
        }
    }

    @Override
    public void start() throws IllegalStateException {
        mMediaPlayer.start();
        notifyOnStart();
    }

    @Override
    public void stop() throws IllegalStateException {
        mMediaPlayer.stop();
        notifyOnStop();
    }

    @Override
    public void pause() throws IllegalStateException {
        mMediaPlayer.pause();
        notifyOnPause();
    }

    @Override
    public int getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        mMediaPlayer.seekTo((int) msec);
        notifyOnSeek();
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void release() {
        mMediaPlayer.release();
        notifyOnRelease();
    }

    @Override
    public void reset() {
        mMediaPlayer.reset();
        notifyOnReset();
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public int getAudioSessionId() {
        return -1;
    }

    private MediaPlayer.OnCompletionListener mOnCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            notifyOnComplete();
        }
    };

    private MediaPlayer.OnPreparedListener mOnPrepared = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            notifyOnPrepared();
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdate = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            notifyOnBufferingUpdate(percent);
        }
    };

    private MediaPlayer.OnErrorListener mOnError = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return notifyOnError(what, extra);
        }
    };

    private MediaPlayer.OnInfoListener mOnInfo = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return notifyOnError(what, extra);
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChanged = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            notifyOnVideoSizeChanged(width, height, 1, 1);
        }
    };

    private MediaPlayer.OnSeekCompleteListener mOnSeekComplete = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            notifyOnSeekComplete();
        }
    };


}
