package dai.android.video.iptv;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import dai.android.core.log.Logger;
import dai.android.media.proxy.IMediaPlayer;
import dai.android.media.proxy.IPlayCallBack;
import dai.android.media.proxy.MediaPlayerProxy;
import dai.android.video.iptv.data.Address;
import dai.android.video.iptv.data.Category;
import dai.android.video.iptv.data.Source;
import dai.android.video.iptv.data.UrlBox;
import dai.android.video.iptv.module.AddressManager;
import dai.android.video.iptv.module.ILoader;
import dai.android.video.iptv.player.DtMediaPlay;

public class MainActivity extends Activity {
    private static final String TAG = "ActivityMain";

    private SurfaceView mDisplaySurface;
    // MediaPlayer mMediaPlayer;

    private IMediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDisplaySurface = findViewById(R.id.displaySurface);
        mDisplaySurface.getHolder().addCallback(mCallBack);

        // LivePlayerManager.get().createMedia();
        AddressManager.get().setLoader(mLoader);

        /// mMediaPlayer = new MediaPlayer(this, false);
        DtMediaPlay dtPlayer = new DtMediaPlay(this);
        mMediaPlayer = new MediaPlayerProxy(dtPlayer);
        // mMediaPlayer = new MediaPlayerProxy();
        mMediaPlayer.setPlayCallBack(mPlayCallBack);
    }

    private IPlayCallBack mPlayCallBack = new IPlayCallBack() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {

        }

        @Override
        public void onCompletion(IMediaPlayer mp) {

        }

        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onPause(IMediaPlayer mp) {

        }

        @Override
        public void onPrepared(IMediaPlayer mp) {
            Logger.d(TAG, "this prepared");
            if (null != mMediaPlayer) {
                mMediaPlayer.start();
            }
        }

        @Override
        public void onPrepare(IMediaPlayer mp) {

        }

        @Override
        public void onSeekComplete(IMediaPlayer mp) {

        }

        @Override
        public void onSeek(IMediaPlayer mp) {

        }

        @Override
        public void onStart(IMediaPlayer mp) {

        }

        @Override
        public void onStop(IMediaPlayer mp) {

        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {

        }

        @Override
        public void onReset() {

        }

        @Override
        public void onRelease() {

        }
    };

    private void startMediaPlayer(String url) {
        try {
            if (null == mMediaPlayer) {
                return;
            }
            if (mMediaPlayer.isPlaying()) {
                return;
            }

            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setDisplay(mDisplaySurface.getHolder());
            mMediaPlayer.prepareAsync();
            // mMediaPlayer.start();

        } catch (Exception ex) {
        }
    }

    private void setDefaultDataSource() {
        List<Source> sources = AddressManager.get().getSources(UrlBox.STR_TELECOM);
        // List<Source> sources = AddressManager.get().getSources(UrlBox.STR_MISC);
        if (null != sources && !sources.isEmpty()) {
            Source item = sources.get(0);

            List<Category> categories = item.getCategories();
            if (null != categories && !categories.isEmpty()) {
                Category category = categories.get(2);
                Address address = category.getAddress().get(7);
                Logger.d(TAG, "play address: " + address.getAddress());
                try {
                    mMediaPlayer.setDataSource(address.getAddress());
                    mMediaPlayer.setDisplay(mDisplaySurface.getHolder());
                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.start();
                } catch (Exception e) {
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mMediaPlayer) {
            mMediaPlayer.release();
        }
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // private function and filed

    private ILoader mLoader = new ILoader() {
        @Override
        public void onAddressLoader() {
            //setDefaultDataSource();
        }
    };


    private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Logger.d(TAG, "surface created");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //startMediaPlayer("rtsp://113.136.42.41:554/PLTV/88888888/224/3221226131/10000100000000060000000001817714_0.smil");
            // startMediaPlayer("http://v.cic.tsinghua.edu.cn:8080/live/tsinghuatv.flv");
            // 大白兔
            // startMediaPlayer("rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov");
            //startMediaPlayer("http://alhls.cdn.zhanqi.tv/zqlive/264147_VU1iK.m3u8");

            //startMediaPlayer("http://dlhls.cdn.zhanqi.tv/zqlive/96851_ZeePd.m3u8");
            // startMediaPlayer("http://dlhls.cdn.zhanqi.tv/zqlive/123407_XhQs9_1024/index.m3u8");
            // startMediaPlayer("http://dlhls.cdn.zhanqi.tv/zqlive/123407_XhQs9_1024/index.m3u8");
            startMediaPlayer("rtsp://113.136.42.39:554/PLTV/88888888/224/3221226033/10000100000000060000000001580493_0.smil");

            // 峨嵋电影
            /// startMediaPlayer("http://scgctvshow.sctv.com/hdlive/emei/3.m3u8");

            // 虎牙电影 1
            // startMediaPlayer("http://aldirect.hls.huya.com/huyalive/30765679-2504742278-10757786168918540288-3049003128-10057-A-0-1_1200.m3u8");

            // 澳门莲花
            // startMediaPlayer("rtmp://live-rtmp.lotustv.duolaibo.cn/lotustv/5562e9e4d409d24c9600075c");

            // 环球电视台
            // startMediaPlayer("http://live-cdn.xzxwhcb.com/hls/sn88wrar.m3u8");

            // 星卫电视台
            // startMediaPlayer("http://59.120.242.104:9000/live/live10.m3u8");

            // 龙华经典
            // startMediaPlayer("http://61.216.177.73/sta/ch10119014.m3u8");

            // 纪实频道
            // startMediaPlayer("rtmp://58.200.131.2:1935/livetv/docuchina");

            // cmc usa
            // startMediaPlayer("http://cmctv.ios.internapcdn.net/cmctv_vitalstream_com/live_1/CMCUSA/CCURstream0.m3u8");

            // 东北银
            // startMediaPlayer("https://new.jsyunbf.com/20180331/Ykup4R2v/index.m3u8 ");

            // 动作电影
            // startMediaPlayer("http://117.187.29.38:6060/000000001000/8103864434730665389/1.m3u8?");

            // CCTV6
            // startMediaPlayer("http://ivi.bupt.edu.cn/hls/cctv6.m3u8");

            // 也许是  流浪地球
            // startMediaPlayer("https://zk.wb699.com/2019/02/07/ar7U7LW69WOfokCb/playlist.m3u8");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };


}
