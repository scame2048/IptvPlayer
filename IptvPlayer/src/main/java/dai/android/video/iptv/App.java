package dai.android.video.iptv;

import android.app.Application;

import dai.android.video.iptv.module.AddressManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AddressManager.get(getApplicationContext()).fetch();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
