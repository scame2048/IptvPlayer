package dai.android.video.iptv.module;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dai.android.video.iptv.data.Source;
import dai.android.video.iptv.data.UrlBox;
import dai.android.video.iptv.network.IHttpCallBack;
import dai.android.video.iptv.network.UrlFetcher;
import dai.android.video.iptv.utility.Logger;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class AddressManager {
    private static final String TAG = AddressManager.class.getSimpleName();

    private final Map<String, List<Source>> mSources = new HashMap<>();

    public AddressManager() {
        mSources.clear();
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

            for (UrlBox.Url url : values) {
                String strUrl = UrlBox.BASE_URL + url.path;

                UrlFetcher fetcher = new UrlFetcher();
                fetcher.doRequest(strUrl, new IHttpCallBack() {
                    @Override
                    public void onSuccess(String url, Response response) {
                        if (null == response || !response.isSuccessful()) {
                            Logger.e(TAG, "bad response from: " + url);
                            return;
                        }

                        ResponseBody body = response.body();
                        if (null == body) {
                            Logger.e(TAG, "null response body");
                            return;
                        }

                        try {
                            String strBody = body.string();
                            convert(strKey, strBody);
                        } catch (IOException e) {
                            Logger.e(TAG, "some error:", e);
                        }
                    }

                    @Override
                    public void onFailed(String url, Exception e) {
                        Logger.e(TAG, "request error from: " + url, e);
                    }
                });

            }
        }
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
