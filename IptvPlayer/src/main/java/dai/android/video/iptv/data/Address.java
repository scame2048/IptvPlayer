package dai.android.video.iptv.data;

public final class Address {
    // 频道名称, 例如 cctv 1
    private String channel;

    // 清晰度 0:普清  1:高清  10:备用
    private int definition;

    // 是否可用 0:不可用  1:可用
    private int enable;

    // 真正播放地址
    private String address;

    public String getChannel() {
        return channel;
    }

    public int getDefinition() {
        return definition;
    }

    public int getEnable() {
        return enable;
    }


    public String getAddress() {
        return address;
    }
}
