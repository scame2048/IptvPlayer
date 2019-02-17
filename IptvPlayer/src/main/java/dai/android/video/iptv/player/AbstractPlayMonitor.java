package dai.android.video.iptv.player;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public abstract class AbstractPlayMonitor implements IPlayMonitor {

    @Override
    public void onBufferingUpdate(IMediaPlayer player, int i) {
    }

    @Override
    public void onCompletion(IMediaPlayer player) {
    }

    @Override
    public boolean onError(IMediaPlayer player, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer player, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer player) {
    }

    @Override
    public void onSeekComplete(IMediaPlayer player) {
    }

    @Override
    public void onTimedText(IMediaPlayer player, IjkTimedText ijkTimedText) {
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer player, int i, int i1, int i2, int i3) {
    }
}
