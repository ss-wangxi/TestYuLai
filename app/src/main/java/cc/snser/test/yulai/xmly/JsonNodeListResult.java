package cc.snser.test.yulai.xmly;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import cc.snser.test.yulai.App;
import cc.snser.test.yulai.util.RUtils;
import cc.snser.test.yulai.util.XLog;

/**
 * Created by Snser on 2017/1/15.
 */

public class JsonNodeListResult {
    protected static final int CODE_SUCC = 200;

    public static final long ID_UNKNOWN = Integer.MIN_VALUE;

    public long id;
    public int code;
    public String title;
    public int mode;
    public int total; //当前node所有item的个数，不限于当次请求
    public int skip;
    public ArrayList<JsonNodeItem> items;

    public JsonNodeListResult() {
        this.code = CODE_SUCC;
        this.total = 0;
        this.mode = Server.LIST_UNKNOWN;
        this.items = new ArrayList<>();
    }

    public static class JsonNodeItem {
        public long id;
        public long parentId; //父节点的nodeid
        public long rootId; //根节点的nodeid
        public String icon;
        public String title;
        transient public int position; //在列表中的位置
        public int positionOriginal; //在原始列表中的位置（最近播放列表中使用）
        public int progress; //播放进度
        transient public int iconResId;
        transient public boolean actived;
        transient public JsonNodeListResult parentInfo;

        transient public Class<?> rawcls; //原始数据类型
        transient public Object rawData;  //原始数据（目前只有Track会写这个字段）

        public JsonNodeItem(Object rawData, long id, long parentId, long rootId, String title) {
            this.rawcls = rawData.getClass();
            this.rawData = (rawcls == Track.class ? rawData : null); //只有track才需要
            this.id = id;
            this.parentId = parentId;
            this.rootId = rootId;
            this.title = title;
        }
    }


    /* parse from ximalaya begin */
    static JsonNodeListResult parse(CategoryList categoryList) {
        JsonNodeListResult result = null;
        if (categoryList != null) {
            result = new JsonNodeListResult();
            result.id = ID_UNKNOWN;
            result.mode = Server.LIST_DIRECTORY;
            result.title = "";
            for (Category category : categoryList.getCategories()) {
                XLog.d("Snser", category.getId() + " " + category.getCategoryName() + " " + category.getCoverUrlMiddle());
                result.items.add(new JsonNodeItem(category, category.getId(), result.id, category.getId(), category.getCategoryName()));
            }
            result.total = result.items.size();
        }
        return result;
    }

    static JsonNodeListResult parse(JsonNodeItem parentNode, TagList tagList) {
        JsonNodeListResult result = null;
        if (tagList != null) {
            result = new JsonNodeListResult();
            result.id = ID_UNKNOWN;
            result.mode = Server.LIST_DIRECTORY;
            result.title = parentNode.title;
            for (Tag tag : tagList.getTagList()) {
                result.items.add(new JsonNodeItem(tag, ID_UNKNOWN, result.id, parentNode.rootId, tag.getTagName()));
            }
            result.total = result.items.size();
        }
        return result;
    }

    static JsonNodeListResult parse(JsonNodeItem parentNode, AlbumList albumList) {
        JsonNodeListResult result = null;
        if (albumList != null) {
            result = new JsonNodeListResult();
            result.id = ID_UNKNOWN;
            result.mode = Server.LIST_DIRECTORY;
            result.title = parentNode.title;
            for (Album album : albumList.getAlbums()) {
                result.items.add(new JsonNodeItem(album, album.getId(), result.id, parentNode.rootId, album.getAlbumTitle()));
            }
            result.total = albumList.getTotalCount();
        }
        return result;
    }

    static JsonNodeListResult parse(JsonNodeItem parentNode, TrackList trackList) {
        JsonNodeListResult result = null;
        if (trackList != null) {
            result = new JsonNodeListResult();
            result.id = ID_UNKNOWN;
            result.mode = Server.LIST_DIRECTORY;
            result.title = parentNode.title;
            for (Track track : trackList.getTracks()) {
                result.items.add(new JsonNodeItem(track, track.getDataId(), result.id, parentNode.rootId, track.getTrackTitle()));
            }
            result.total = trackList.getTotalCount();
        }
        return result;
    }
    /* parse from ximalaya end */


    public static JsonNodeListResult parse(int id, String strNodeListResult) {
        JsonNodeListResult jsonResult = null;
        try {
            final GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
            jsonResult = gsonBuilder.create().fromJson(strNodeListResult, JsonNodeListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return build(id, jsonResult);
    }

    public static JsonNodeListResult parse(int id, InputStreamReader isrNodeInfoResult) {
        JsonNodeListResult jsonResult = null;
        try {
            final GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
            jsonResult = gsonBuilder.create().fromJson(isrNodeInfoResult, JsonNodeListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return build(id, jsonResult);
    }

    public static JsonNodeListResult build(int id, JsonNodeListResult result) {
        if (result != null && result.checkVaild()) {
            result.id = id;
            int position = -1;
            final Iterator<JsonNodeItem> iter = result.items.iterator();
            while (iter.hasNext()) {
                final JsonNodeItem item = iter.next();
                if (!TextUtils.isEmpty(item.icon)) {
                    item.iconResId = RUtils.drawable.name(App.getAppContext(), item.icon);
                }
                item.position = ++position;
//                if (id != RecentController.ID) {
                    item.positionOriginal = item.position;
//                    item.parentId = id;
//                }
            }
            return result;
        } else {
            return null;
        }
    }

    public int size() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    public void clear() {
        this.total = 0;
        this.skip = 0;
        if (this.items != null) {
            this.items.clear();
        }
    }

    /**
     * 获取列表内最后一个节点的id
     * @return
     */
    public long getLastNode() {
        if (items != null && items.size() > 0) {
            return items.get(items.size() - 1).id;
        } else {
            return Server.LAST_DEFAULT;
        }
    }

    public int getType() {
        return mode;
    }

    /**
     * 合并两个列表
     * @param newResult
     * @return
     */
    public JsonNodeListResult merge(JsonNodeListResult newResult) {
        if (this.items != null
             && newResult != null
             && newResult.items != null
             && newResult.mode != Server.LIST_UNKNOWN
             && (this.mode == Server.LIST_UNKNOWN || this.mode == newResult.mode)) {
            this.id = newResult.id;
            this.title = newResult.title;
            this.total = newResult.total;
            this.mode = newResult.mode;
            int position = this.size() - 1;
            for (JsonNodeItem item : newResult.items) {
                item.position = ++position;
//                if (this.id != RecentController.ID) {
                    item.positionOriginal = item.position;
                    item.parentId = this.id;
//                }
                this.items.add(item);
            }
        }
        return this;
    }

    /**
     * 检查数据是否合法
     * @return
     */
    private boolean checkVaild() {
        return code == CODE_SUCC
                && (mode == Server.LIST_DIRECTORY || mode == Server.LIST_ARTICLE/* || mode == Server.LIST_RECENT*/)
                && total > 0
                && skip >= 0
                && (items != null && total >= items.size());
    }

}
