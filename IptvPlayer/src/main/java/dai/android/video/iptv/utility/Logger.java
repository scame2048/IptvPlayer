package dai.android.video.iptv.utility;

import android.util.Log;

public abstract class Logger {

    public static void v(String tag, String message, Throwable tr) {
        Log.v(tag, message, tr);
    }

    public static void v(String tag, String message) {
        v(tag, message, null);
    }

    public static void d(String tag, String message, Throwable tr) {
        Log.d(tag, message, tr);
    }

    public static void d(String tag, String message) {
        d(tag, message, null);
    }

    public static void i(String tag, String message, Throwable tr) {
        Log.i(tag, message, tr);
    }

    public static void i(String tag, String message) {
        i(tag, message, null);
    }

    public static void w(String tag, String message, Throwable tr) {
        Log.w(tag, message, tr);
    }

    public static void w(String tag, String message) {
        w(tag, message, null);
    }

    public static void e(String tag, String message, Throwable tr) {
        Log.e(tag, message, tr);
    }

    public static void e(String tag, String message) {
        e(tag, message, null);
    }

}
