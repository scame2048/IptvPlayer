package dai.android.video.iptv.data;

import java.util.List;

public class Source {

    private String name;
    private int version;
    private List<Category> list;

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public List<Category> getCategories() {
        return list;
    }

}
