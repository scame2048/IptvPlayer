package dai.android.core.utility;

import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.InputStream;

public final class Assets {

    public static String readFromAssets(AssetManager manager, String file) {
        if (null == manager || TextUtils.isEmpty(file)) {
            return null;
        }
        try {
            InputStream is = manager.open(file);
            int size = is.available();
            byte[] bytes = new byte[size];
            int iread = is.read(bytes);
            is.close();
            return new String(bytes);
        } catch (Exception e) {
        }
        return null;
    }
}
