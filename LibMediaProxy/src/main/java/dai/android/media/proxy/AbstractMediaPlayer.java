package dai.android.media.proxy;

public abstract class AbstractMediaPlayer implements IMediaPlayer {

    private IPlayCallBack mPlayCallBack;

    public void resetListener() {
        mPlayCallBack = null;
    }

    public final void setCallBack(IPlayCallBack callBack) {
        mPlayCallBack = callBack;
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    protected final void notifyOnBufferingUpdate(int percent) {
        if (null != mPlayCallBack) {
            mPlayCallBack.onBufferingUpdate(this, percent);
        }
    }

    protected final void notifyOnComplete() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onCompletion(this);
        }
    }

    protected final boolean notifyOnError(int what, int extra) {
        if (null != mPlayCallBack) {
            return mPlayCallBack.onError(this, what, extra);
        }
        return false;
    }

    protected final boolean notifyOnInfo(int what, int extra) {
        if (null != mPlayCallBack) {
            return mPlayCallBack.onInfo(this, what, extra);
        }
        return false;
    }

    protected final void notifyOnPause() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onPause(this);
        }
    }

    protected final void notifyOnPrepared() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onPrepared(this);
        }
    }

    protected final void notifyOnPrepare() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onPrepare(this);
        }
    }

    protected final void notifyOnSeekComplete() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onSeekComplete(this);
        }
    }

    protected final void notifyOnSeek() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onSeek(this);
        }
    }

    protected final void notifyOnStart() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onStart(this);
        }
    }

    protected final void notifyOnStop() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onStop(this);
        }
    }

    protected final void notifyOnVideoSizeChanged(int width, int height, int sar_num, int sar_den) {
        if (null != mPlayCallBack) {
            mPlayCallBack.onVideoSizeChanged(this, width, height, sar_num, sar_den);
        }
    }

}
