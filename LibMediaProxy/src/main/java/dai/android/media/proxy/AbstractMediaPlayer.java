package dai.android.media.proxy;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public abstract class AbstractMediaPlayer implements IMediaPlayer {

    private IPlayCallBack mPlayCallBack;
    private final HandlerThread mWorkThread = new HandlerThread("PlayerJavaThread");
    private final Handler H;

    public AbstractMediaPlayer() {
        mWorkThread.start();
        H = new MyHandle(mWorkThread.getLooper());
    }


    public void resetListener() {
        mPlayCallBack = null;
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    protected final void notifyOnBufferingUpdate(int percent) {
        Message message = Message.obtain();
        message.what = WHAT_BUFFERING_UPDATE;
        message.arg1 = percent;
        H.sendMessage(message);
    }

    private void onBufferingUpdateInner(int percent) {
        if (null != mPlayCallBack) {
            mPlayCallBack.onBufferingUpdate(this, percent);
        }
    }

    protected final void notifyOnComplete() {
        H.sendEmptyMessage(WHAT_ON_COMPLETE);
    }

    private void onCompleteInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onCompletion(this);
        }
    }

    protected final boolean notifyOnError(int what, int extra) {
        Message message = Message.obtain();
        message.what = WHAT_ON_ERROR;
        message.arg1 = what;
        message.arg2 = extra;
        H.sendMessage(message);
        return true;
    }

    private void onErrorInner(int what, int extra) {
        if (null != mPlayCallBack) {
            mPlayCallBack.onError(this, what, extra);
        }
    }

    protected final boolean notifyOnInfo(int what, int extra) {
        Message message = Message.obtain();
        message.what = WHAT_ON_ERROR;
        message.arg1 = what;
        message.arg2 = extra;
        H.sendMessage(message);
        return true;
    }

    private void onInfoInner(int what, int extra) {
        if (null != mPlayCallBack) {
            mPlayCallBack.onInfo(this, what, extra);
        }
    }

    protected final void notifyOnPause() {
        H.sendEmptyMessage(WHAT_ON_PAUSE);
    }

    private void onPauseInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onPause(this);
        }
    }

    protected final void notifyOnPrepared() {
        H.sendEmptyMessage(WHAT_ON_PREPARED);
    }

    private void onPreparedInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onPrepared(this);
        }
    }

    protected final void notifyOnPrepare() {
        H.sendEmptyMessage(WHAT_ON_PREPARE);
    }

    private void onPrepareInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onPrepare(this);
        }
    }

    protected final void notifyOnSeekComplete() {
        H.sendEmptyMessage(WHAT_ON_SEEK_COMPLETE);
    }

    private void onSeekCompleteInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onSeekComplete(this);
        }
    }

    protected final void notifyOnSeek() {
        H.sendEmptyMessage(WHAT_ON_SEEK);
    }

    private void onSeekInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onSeek(this);
        }
    }

    protected final void notifyOnStart() {
        H.sendEmptyMessage(WHAT_ON_START);
    }

    private void onStartInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onStart(this);
        }
    }

    protected final void notifyOnStop() {
        H.sendEmptyMessage(WHAT_ON_STOP);
    }

    private void onStopInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onStop(this);
        }
    }

    protected final void notifyOnVideoSizeChanged(int width, int height, int sar_num, int sar_den) {
        if (null != mPlayCallBack) {
            mPlayCallBack.onVideoSizeChanged(this, width, height, sar_num, sar_den);
        }
    }

    protected final void notifyOnReset() {
        H.sendEmptyMessage(WHAT_ON_RESET);
    }

    private void onResetInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onReset();
        }
    }

    protected final void notifyOnRelease() {
        H.sendEmptyMessage(WHAT_ON_RELEASE);
    }

    private void onReleaseInner() {
        if (null != mPlayCallBack) {
            mPlayCallBack.onRelease();
        }
    }

    @Override
    public final void setPlayCallBack(IPlayCallBack callBack) {
        mPlayCallBack = callBack;
    }

    private static final int WHAT_ON_START = 0x01;
    private static final int WHAT_BUFFERING_UPDATE = 0x02;
    private static final int WHAT_ON_COMPLETE = 0x03;
    private static final int WHAT_ON_ERROR = 0x04;
    private static final int WHAT_ON_INFO = 0x05;
    private static final int WHAT_ON_PAUSE = 0x06;
    private static final int WHAT_ON_PREPARED = 0x07;
    private static final int WHAT_ON_PREPARE = 0x08;
    private static final int WHAT_ON_SEEK_COMPLETE = 0x09;
    private static final int WHAT_ON_SEEK = 0x0A;
    private static final int WHAT_ON_STOP = 0x0B;
    private static final int WHAT_ON_RESET = 0x0C;
    private static final int WHAT_ON_RELEASE = 0x0D;

    protected void removeHandle() {
        H.removeMessages(WHAT_ON_START);
        H.removeMessages(WHAT_BUFFERING_UPDATE);
        H.removeMessages(WHAT_ON_COMPLETE);
        H.removeMessages(WHAT_ON_ERROR);
        H.removeMessages(WHAT_ON_INFO);
        H.removeMessages(WHAT_ON_PAUSE);
        H.removeMessages(WHAT_ON_PREPARED);
        H.removeMessages(WHAT_ON_PREPARE);
        H.removeMessages(WHAT_ON_SEEK_COMPLETE);
        H.removeMessages(WHAT_ON_SEEK);
        H.removeMessages(WHAT_ON_STOP);
        H.removeMessages(WHAT_ON_RESET);
        H.removeMessages(WHAT_ON_RELEASE);
    }

    protected void handleMessage(Message message) {
        if (null == message) {
            return;
        }
        switch (message.what) {
            case WHAT_ON_START: {
                onStartInner();
                break;
            }
            case WHAT_BUFFERING_UPDATE: {
                int percent = message.arg1;
                onBufferingUpdateInner(percent);
                break;
            }
            case WHAT_ON_COMPLETE: {
                onCompleteInner();
                break;
            }
            case WHAT_ON_ERROR: {
                int what = message.arg1;
                int extra = message.arg2;
                onErrorInner(what, extra);
                break;
            }
            case WHAT_ON_INFO: {
                int what = message.arg1;
                int extra = message.arg2;
                onInfoInner(what, extra);
                break;
            }
            case WHAT_ON_PAUSE: {
                onPauseInner();
                break;
            }
            case WHAT_ON_PREPARED: {
                onPreparedInner();
                break;
            }
            case WHAT_ON_PREPARE: {
                onPrepareInner();
                break;
            }
            case WHAT_ON_SEEK_COMPLETE: {
                onSeekCompleteInner();
                break;
            }
            case WHAT_ON_SEEK: {
                onSeekInner();
                break;
            }
            case WHAT_ON_STOP: {
                onStopInner();
                break;
            }
            case WHAT_ON_RESET: {
                onResetInner();
                break;
            }
            case WHAT_ON_RELEASE: {
                onReleaseInner();
                break;
            }
        }
    }

    private class MyHandle extends Handler {
        MyHandle(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            AbstractMediaPlayer.this.handleMessage(msg);
        }
    }
}
