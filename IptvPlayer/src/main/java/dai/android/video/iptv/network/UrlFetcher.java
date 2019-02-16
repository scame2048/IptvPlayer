package dai.android.video.iptv.network;

import android.text.TextUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dai.android.video.iptv.utility.Logger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UrlFetcher {
    private static final String TAG = UrlFetcher.class.getSimpleName();

    private static final int DEF_TIMEOUT = 45; // second

    private final OkHttpClient mOkHttpClient;

    public UrlFetcher() {
        mOkHttpClient = makeOkHttpClient();
    }


    public void doRequest(final String url, final IHttpCallBack callBack) {
        Logger.d(TAG, "request url: " + url);
        if (TextUtils.isEmpty(url)) {
            Logger.e(TAG, "bad url, skip this request");
            return;
        }

        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null != callBack) {
                    callBack.onFailed(url, e);
                } else {
                    Logger.e(TAG, "failed request url: " + url, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != callBack) {
                    callBack.onSuccess(url, response);
                } else {
                    Logger.d(TAG, "request success");
                }
            }
        });
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // private static function
    //

    private static TrustManager[] defaultTrustManager() {
        TrustManager[] trustManagers = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        return trustManagers;
    }

    private static SSLSocketFactory makeSSLSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, defaultTrustManager(), new SecureRandom());
            return context.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            Logger.e(TAG, "create SSLSocketFactory failed.", e);
        } catch (KeyManagementException e) {
            Logger.e(TAG, "create KeyManagementException failed.", e);
        }
        return null;
    }

    private static OkHttpClient makeOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEF_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEF_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEF_TIMEOUT, TimeUnit.SECONDS);

        builder.sslSocketFactory(makeSSLSocketFactory());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        return builder.build();
    }

}
