package dai.android.media.proxy;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

public final class MediaPlayerProxy implements IMediaPlayer {
    private final IMediaPlayer mBackEndMediaPlayer;

    public MediaPlayerProxy(IMediaPlayer backEndMediaPlayer) {
        mBackEndMediaPlayer = backEndMediaPlayer;
    }

    public MediaPlayerProxy() {
        this(new AndroidMediaPlayer());
    }

    public IMediaPlayer getInternalMediaPlayer() {
        return mBackEndMediaPlayer;
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        mBackEndMediaPlayer.setDisplay(sh);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setSurface(Surface surface) {
        mBackEndMediaPlayer.setSurface(surface);
    }

    @Override
    public void setDataSource(Context context, Uri uri) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mBackEndMediaPlayer.setDataSource(context, uri);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mBackEndMediaPlayer.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd) throws
            IOException, IllegalArgumentException, IllegalStateException {
        mBackEndMediaPlayer.setDataSource(fd);
    }

    @Override
    public void setDataSource(String path) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mBackEndMediaPlayer.setDataSource(path);
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        mBackEndMediaPlayer.prepareAsync();
    }

    @Override
    public void start() throws IllegalStateException {
        mBackEndMediaPlayer.start();
    }

    @Override
    public void stop() throws IllegalStateException {
        mBackEndMediaPlayer.stop();
    }

    @Override
    public void pause() throws IllegalStateException {
        mBackEndMediaPlayer.pause();
    }

    @Override
    public int getVideoWidth() {
        return mBackEndMediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mBackEndMediaPlayer.getVideoHeight();
    }

    @Override
    public boolean isPlaying() {
        return mBackEndMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        mBackEndMediaPlayer.seekTo(msec);
    }

    @Override
    public long getCurrentPosition() {
        return mBackEndMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mBackEndMediaPlayer.getDuration();
    }

    @Override
    public void release() {
        mBackEndMediaPlayer.release();
    }

    @Override
    public void reset() {
        mBackEndMediaPlayer.reset();
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mBackEndMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public int getAudioSessionId() {
        return mBackEndMediaPlayer.getAudioSessionId();
    }

    @Override
    public void setPlayCallBack(IPlayCallBack callBack) {
        if (null == callBack) {
            mBackEndMediaPlayer.setPlayCallBack(null);
        } else {
            final IPlayCallBack myCallBack = callBack;
            mBackEndMediaPlayer.setPlayCallBack(new IPlayCallBack() {
                @Override
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    myCallBack.onBufferingUpdate(MediaPlayerProxy.this, percent);
                }

                @Override
                public void onCompletion(IMediaPlayer mp) {
                    myCallBack.onCompletion(MediaPlayerProxy.this);
                }

                @Override
                public boolean onError(IMediaPlayer mp, int what, int extra) {
                    return myCallBack.onError(MediaPlayerProxy.this, what, extra);
                }

                @Override
                public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                    return myCallBack.onInfo(MediaPlayerProxy.this, what, extra);
                }

                @Override
                public void onPause(IMediaPlayer mp) {
                    myCallBack.onPause(MediaPlayerProxy.this);
                }

                @Override
                public void onPrepared(IMediaPlayer mp) {
                    myCallBack.onPrepared(MediaPlayerProxy.this);
                }

                @Override
                public void onPrepare(IMediaPlayer mp) {
                    myCallBack.onPrepare(MediaPlayerProxy.this);
                }

                @Override
                public void onSeekComplete(IMediaPlayer mp) {
                    myCallBack.onSeekComplete(MediaPlayerProxy.this);
                }

                @Override
                public void onSeek(IMediaPlayer mp) {
                    myCallBack.onSeek(MediaPlayerProxy.this);
                }

                @Override
                public void onStart(IMediaPlayer mp) {
                    myCallBack.onStart(MediaPlayerProxy.this);
                }

                @Override
                public void onStop(IMediaPlayer mp) {
                    myCallBack.onStop(MediaPlayerProxy.this);
                }

                @Override
                public void onVideoSizeChanged(IMediaPlayer mp,
                                               int width, int height,
                                               int sar_num, int sar_den) {
                    myCallBack.onVideoSizeChanged(
                            MediaPlayerProxy.this, width, height, sar_num, sar_den);

                }

                @Override
                public void onReset() {
                    myCallBack.onReset();
                }

                @Override
                public void onRelease() {
                    myCallBack.onRelease();
                }
            });
        }
    }


}
