package dai.android.video.iptv.module;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dai.android.video.iptv.data.Source;
import dai.android.video.iptv.data.UrlBox;
import dai.android.video.iptv.network.IHttpCallBack;
import dai.android.video.iptv.network.UrlFetcher;
import dai.android.video.iptv.utility.Assets;
import dai.android.video.iptv.utility.Logger;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class AddressManager {
    private static final String TAG = AddressManager.class.getSimpleName();

    private static AddressManager sAddressManager = null;

    public static AddressManager get(Context context) {
        if (null == sAddressManager) {
            synchronized (AddressManager.class) {
                if (null == sAddressManager) {
                    sAddressManager = new AddressManager(context);
                }
            }
        }
        return sAddressManager;
    }

    public static AddressManager get() {
        if (null == sAddressManager) {
            throw new RuntimeException("Call function AddressManager get(Context) first");
        }
        synchronized (AddressManager.class) {
            return sAddressManager;
        }
    }


    private final Map<String, List<Source>> mSources = new HashMap<>();

    private final Context mContext;

    private AddressManager(Context context) {
        mSources.clear();
        mContext = context;
    }

    public Set<String> getFrom() {
        return mSources.keySet();
    }

    public List<Source> getSources(String key) {
        return mSources.get(key);
    }

    public void fetch() {
        if (!mSources.isEmpty()) {
            Logger.d(TAG, "data fetched, skip");
            return;
        }

        Map<String, List<UrlBox.Url>> tmpUrls = UrlBox.getUrls();
        if (null == tmpUrls || tmpUrls.isEmpty()) {
            Logger.w(TAG, "empty original data, skip fetch");
            return;
        }

        for (Map.Entry<String, List<UrlBox.Url>> entry : tmpUrls.entrySet()) {
            final String strKey = entry.getKey();
            if (TextUtils.isEmpty(strKey)) {
                Logger.w(TAG, "empty key, skip");
                continue;
            }

            List<UrlBox.Url> values = entry.getValue();
            if (null == values || values.isEmpty()) {
                Logger.w(TAG, "no any data at UrlBox: " + strKey);
                continue;
            }

            for (final UrlBox.Url u : values) {
                String strUrl = UrlBox.BASE_URL + u.path;

                UrlFetcher fetcher = new UrlFetcher();
                fetcher.doRequest(strUrl, new IHttpCallBack() {
                    @Override
                    public void onSuccess(String url, Response response) {
                        boolean useDefault = false;
                        if (null == response || !response.isSuccessful()) {
                            Logger.e(TAG, "bad response from: " + url);
                            useDefault = true;
                        }

                        ResponseBody body = null;
                        if (!useDefault) {
                            body = response.body();
                            if (null == body) {
                                Logger.e(TAG, "null response body");
                                useDefault = true;
                                return;
                            }
                        }

                        if (!useDefault) {
                            try {
                                String strBody = body.string();
                                convert(strKey, strBody);
                            } catch (IOException e) {
                                Logger.e(TAG, "some error:", e);
                                useDefault = true;
                            }
                        }

                        if (useDefault) {
                            Logger.w(TAG, "user default data at asset/" + u.path);
                            readRawFromAsset(strKey, u);
                        }
                    }

                    @Override
                    public void onFailed(String url, Exception e) {
                        Logger.e(TAG, "request error from: " + url, e);
                        Logger.w(TAG, "user default data at asset/" + u.path);
                        readRawFromAsset(strKey, u);
                    }
                });

            }
        }
    }

    private void readRawFromAsset(String from, UrlBox.Url url) {
        if (TextUtils.isEmpty(from))
            return;
        if (url == null)
            return;

        String raw = Assets.readFromAssets(mContext.getAssets(), url.path);
        convert(from, raw);
    }

    private void convert(String from, String raw) {
        if (TextUtils.isEmpty(from)) {
            Logger.w(TAG, "bad come from");
            return;
        }

        if (TextUtils.isEmpty(raw)) {
            Logger.w(TAG, "bad original response data");
            return;
        }

        Gson gson = new Gson();
        try {
            Source source = gson.fromJson(raw, Source.class);
            List<Source> items = mSources.get(from);
            if (null == items) {
                items = new ArrayList<>();
                mSources.put(from, items);
            }
            items.add(source);

            Logger.d(TAG, "add json object source to: " + from);
        } catch (JsonSyntaxException e) {
            Logger.e(TAG, "convert failed:", e);
        }
    }
}
