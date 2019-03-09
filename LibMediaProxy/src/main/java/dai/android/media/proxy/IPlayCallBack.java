package dai.android.media.proxy;

public interface IPlayCallBack {

    void onBufferingUpdate(IMediaPlayer mp, int percent);

    void onCompletion(IMediaPlayer mp);

    boolean onError(IMediaPlayer mp, int what, int extra);

    boolean onInfo(IMediaPlayer mp, int what, int extra);

    void onPause(IMediaPlayer mp);

    void onPrepared(IMediaPlayer mp);

    void onPrepare(IMediaPlayer mp);

    void onSeekComplete(IMediaPlayer mp);

    void onSeek(IMediaPlayer mp);

    void onStart(IMediaPlayer mp);

    void onStop(IMediaPlayer mp);

    void onVideoSizeChanged(IMediaPlayer mp,
                            int width, int height,
                            int sar_num, int sar_den);

}
