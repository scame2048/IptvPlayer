package dai.android.video.iptv.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UrlBox {

    // 基础数据网址
    // coding.net 无法使用 OkHttp 是直接访问对应 https 文件
    // 改而投向 github
    // public static final String BASE_URL = "https://coding.net/u/gooogle/p/IptvPlayer/git/raw/master/IptvPlayer/src/main/assets/";
    public static final String BASE_URL = "https://raw.githubusercontent.com/emacs1024/IptvPlayer/master/IptvPlayer/src/main/assets/";

    public static final String STR_MOBILE = "中国移动";
    public static final String STR_TELECOM = "中国电信";
    public static final String STR_UNICOM = "中国联通";
    public static final String STR_MISC = "混合";

    private static Map<String, List<Url>> sUrls = new HashMap<>();

    static {
        // 中国移动源
        List<Url> mobile = new ArrayList<>();
        {
            Url url = new Url();
            url.name = "江苏移动 - 001";
            url.path = "source/ChinaMobile/025_001.json";
            mobile.add(url);
        }

        // 中国电信源
        List<Url> telecom = new ArrayList<>();
        {
            Url url = new Url();
            url.name = "陕西电信 - 001";
            url.path = "source/ChinaTelecom/029_001.json";
            telecom.add(url);
        }


        // 中国联通
        List<Url> unicom = new ArrayList<>();
        {
            Url url = new Url();
            url.name = "山东联通 - 001";
            url.path = "source/ChinaUnicom/0531_001.json";
            unicom.add(url);
        }


        // 混合
        List<Url> miscs = new ArrayList<>();
        {
            Url url = new Url();
            url.name = "混合 - 001";
            url.path = "source/misc/misc_001.json";
            miscs.add(url);
        }


        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        sUrls.put(STR_MOBILE, mobile);
        sUrls.put(STR_TELECOM, telecom);
        sUrls.put(STR_UNICOM, unicom);
        sUrls.put(STR_MISC, miscs);
    }

    public static Map<String, List<Url>> getUrls() {
        return sUrls;
    }


    public static class Url {
        public String name;
        public String path;
    }

}
