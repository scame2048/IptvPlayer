package dai.android.video.iptv;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.List;

import dai.android.video.iptv.data.Address;
import dai.android.video.iptv.data.Category;
import dai.android.video.iptv.data.Source;
import dai.android.video.iptv.data.UrlBox;
import dai.android.video.iptv.module.AddressManager;
import dai.android.video.iptv.utility.Logger;
import dai.android.video.iptv.widget.LiveVideoView;

public class MainActivity extends Activity {
    private static final String TAG = "ActivityMain";

    private LiveVideoView mVideoView;

    private HandlerThread mWorkThread;
    private Handler H;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWorkThread = new HandlerThread(TAG);
        mWorkThread.start();
        H = new Handler(mWorkThread.getLooper());

        setContentView(R.layout.activity_main);
        mVideoView = findViewById(R.id.videoView);


        H.postDelayed(new Runnable() {
            @Override
            public void run() {
                // now let's set data source
                setDefaultDataSource();
            }
        }, 8000);
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
                mVideoView.setDataSource(address.getAddress());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != mVideoView) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mVideoView) {
            mVideoView.release();
        }
    }


}
