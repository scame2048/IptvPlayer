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

public interface IMediaPlayer {

    void setDisplay(SurfaceHolder sh);

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    void setSurface(Surface surface);

    void setDataSource(Context context, Uri uri) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(Context context, Uri uri, Map<String, String> headers) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(FileDescriptor fd) throws
            IOException, IllegalArgumentException, IllegalStateException;

    void setDataSource(String path) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void prepareAsync() throws IllegalStateException;

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;

    void pause() throws IllegalStateException;

    int getVideoWidth();

    int getVideoHeight();

    boolean isPlaying();

    void seekTo(long msec) throws IllegalStateException;

    long getCurrentPosition();

    long getDuration();

    void release();

    void reset();

    void setVolume(float leftVolume, float rightVolume);

    int getAudioSessionId();

    void setPlayCallBack(IPlayCallBack callBack);
}
