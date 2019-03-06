package dai.android.video.iptv.client.audio;

import dai.android.video.iptv.client.stream.RtpStream;

public abstract class AudioStream extends RtpStream {
    private final static String TAG = AudioStream.class.getSimpleName();

    protected void recombinePacket(StreamPacks streamPacks) {
    }
}
