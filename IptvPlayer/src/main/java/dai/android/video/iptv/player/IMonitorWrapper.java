package dai.android.video.iptv.player;

public interface IMonitorWrapper<T extends IPlayMonitor> {
    void doAction(T action);
}
