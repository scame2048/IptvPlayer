package dai.android.video.iptv;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import dai.android.video.iptv.data.Address;
import dai.android.video.iptv.data.Category;
import dai.android.video.iptv.data.Source;
import dai.android.video.iptv.data.UrlBox;
import dai.android.video.iptv.module.AddressManager;
import dai.android.video.iptv.module.ILoader;
import dai.android.video.iptv.player.AbstractPlayMonitor;
import dai.android.video.iptv.player.LivePlayerManager;
import dai.android.video.iptv.utility.Logger;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends Activity {
    private static final String TAG = "ActivityMain";

    private SurfaceView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mVideoView = findViewById(R.id.videoView);
        mVideoView.getHolder().addCallback(mCallBack);

        LivePlayerManager.get().createMedia();
        AddressManager.get().setLoader(mLoader);
    }

    private void setDefaultDataSource() {
        List<Source> sources = AddressManager.get().getSources(UrlBox.STR_TELECOM);
        if (null != sources && !sources.isEmpty()) {
            Source item = sources.get(0);

            List<Category> categories = item.getCategories();
            if (null != categories && !categories.isEmpty()) {
                Category category = categories.get(0);
                Logger.v(TAG, "category: " + category.getCategory());
                Address address = category.getAddress().get(0);
                Logger.d(TAG, "play address: " + address.getAddress());
                LivePlayerManager.get().setDataSource(address.getAddress());
                LivePlayerManager.get().add(mPlayerMonitor);
                LivePlayerManager.get().prepareAsync();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        LivePlayerManager.get().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LivePlayerManager.get().release();
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // private function and filed

    private ILoader mLoader = new ILoader() {
        @Override
        public void onAddressLoader() {
            setDefaultDataSource();
        }
    };

    private AbstractPlayMonitor mPlayerMonitor = new AbstractPlayMonitor() {
        @Override
        public void onBufferingUpdate(IMediaPlayer player, int i) {
            //Logger.d(TAG, "onBufferingUpdate: " + i);
        }

        @Override
        public void onCompletion(IMediaPlayer player) {
            Logger.d(TAG, "[onCompletion]");
        }

        @Override
        public boolean onError(IMediaPlayer player, int what, int extra) {
            Logger.d(TAG, "[onError( " + what + ", " + extra + " )]");
            return true;
        }

        @Override
        public void onPrepared(IMediaPlayer player) {
            Logger.v(TAG, "onPrepared");
            LivePlayerManager.get().start();
        }
    };

    private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Logger.v(TAG, "surface created");
            LivePlayerManager.get().setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };


}
