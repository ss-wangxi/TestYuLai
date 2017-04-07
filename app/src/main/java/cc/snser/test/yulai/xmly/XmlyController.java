package cc.snser.test.yulai.xmly;

import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cc.snser.test.yulai.App;
import cc.snser.test.yulai.util.XLog;

/**
 * Created by wangxi-xy on 2017/4/6.
 */

public class XmlyController implements IXmPlayerStatusListener {
    static final String TAG = "XmlyController";

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final long TIMEOUT_REQUEST_MILLIS = 20 * 1000;

    private CommonRequest mServer;
    private XmPlayerManager mPlayerManager;

    private XmlyController() {
    }

    private static class SingletonHolder {
        static XmlyController INSTANCE = new XmlyController();
    }

    public static XmlyController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        mServer = CommonRequest.getInstanse();
        mServer.init(App.getAppContext(), "4d8e605fa7ed546c4bcb33dee1381179");

        mPlayerManager = XmPlayerManager.getInstance(App.getAppContext());
        mPlayerManager.init();

        mPlayerManager.addPlayerStatusListener(this);
        mPlayerManager.setOnConnectedListerner(new XmPlayerManager.IConnectListener() {
            @Override
            public void onConnected() {
                //mServer.setDefaultPagesize(50);
//                mPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE);
                XLog.d(TAG, "XmPlayerManager onConnected");
            }
        });
    }


    /* IXmPlayerStatusListener Begin */
    @Override
    public void onPlayStart() {
    }

    @Override
    public void onPlayPause() {
    }

    @Override
    public void onPlayStop() {
    }

    @Override
    public void onSoundPlayComplete() {
    }

    @Override
    public void onSoundPrepared() {
    }

    @Override
    public void onSoundSwitch(PlayableModel oldModel, PlayableModel newModel) {
    }

    @Override
    public void onBufferingStart() {
    }

    @Override
    public void onBufferingStop() {
    }

    @Override
    public void onBufferProgress(int percent) {
    }

    @Override
    public void onPlayProgress(int currentPos, int duration) {
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }
    /* IXmPlayerStatusListener End */


    /* RequestData Begin */

    /**
     * 获取目录节点的列表信息
     * @param parentNode 目录节点
     * @param last 已经获取到的列表index，如果last>0，则会从last+1位置开始获取列表
     * @return 列表信息
     */
    public JsonNodeListResult requestNodeList(JsonNodeListResult.JsonNodeItem parentNode, int last) {
        if (parentNode == null || parentNode.rawData == null) {
            return JsonNodeListResult.parse(null, requestCategoryList());
        } else if (parentNode.rawcls == Category.class) {
            return JsonNodeListResult.parse(parentNode, requestTagList((Category)parentNode.rawData));
        } else if (parentNode.rawcls == Tag.class) {
            return JsonNodeListResult.parse(parentNode, requestAlbumList((Tag)parentNode.rawData, parentNode.rootId, last));
        } else if (parentNode.rawcls == Album.class) {
            return JsonNodeListResult.parse(parentNode, requestTrackList((Album)parentNode.rawData, last));
        } else {
            return null;
        }
    }

    private CategoryList requestCategoryList() {
        final DataCallBack<CategoryList> callBack = new DataCallBack<>();
        callBack.lock();
        try {
            CommonRequest.getCategories(null, callBack);
            callBack.waitCallback(TIMEOUT_REQUEST_MILLIS);
        } finally {
            callBack.unlock();
        }
        return callBack.getData();
    }

    private TagList requestTagList(Category category) {
        final DataCallBack<TagList> callBack = new DataCallBack<>();
        callBack.lock();
        try {
            final Map<String, String> params = new HashMap<>();
            params.put(DTransferConstants.CATEGORY_ID, String.valueOf(category.getId()));
            params.put(DTransferConstants.TYPE, "0");
            CommonRequest.getTags(params, callBack);
            callBack.waitCallback(TIMEOUT_REQUEST_MILLIS);
        } finally {
            callBack.unlock();
        }
        return callBack.getData();
    }

    private AlbumList requestAlbumList(Tag tag, long categoryId, int last) {
        final DataCallBack<AlbumList> callBack = new DataCallBack<>();
        callBack.lock();
        try {
            final int pageSize = DEFAULT_PAGE_SIZE;
            final int pageId = last > 0 ? (last + 1) / pageSize : 1;
            final Map<String, String> params = new HashMap<>();
            params.put(DTransferConstants.CATEGORY_ID, String.valueOf(categoryId));
            params.put(DTransferConstants.TAG_NAME, tag.getTagName());
            params.put(DTransferConstants.CALC_DIMENSION ,"1");
            params.put(DTransferConstants.PAGE_SIZE, String.valueOf(pageSize));
            params.put(DTransferConstants.PAGE, String.valueOf(pageId));
            CommonRequest.getAlbumList(params, callBack);
            callBack.waitCallback(TIMEOUT_REQUEST_MILLIS);
        } finally {
            callBack.unlock();
        }
        return callBack.getData();
    }

    private TrackList requestTrackList(Album album, int last) {
        final DataCallBack<TrackList> callBack = new DataCallBack<>();
        callBack.lock();
        try {
            final int pageSize = DEFAULT_PAGE_SIZE;
            final int pageId = last > 0 ? (last + 1) / pageSize : 1;
            final Map<String, String> params = new HashMap<>();
            params.put(DTransferConstants.ALBUM_ID, String.valueOf(album.getId()));
            params.put(DTransferConstants.PAGE_SIZE, String.valueOf(pageSize));
            params.put(DTransferConstants.PAGE, String.valueOf(pageId));
            CommonRequest.getTracks(params, callBack);
            callBack.waitCallback(TIMEOUT_REQUEST_MILLIS);
        } finally {
            callBack.unlock();
        }
        return callBack.getData();
    }

    /**
     * 获取叶子节点的详情
     * @param node 叶子节点
     * @return 详情
     */
    public static JsonNodeInfoResult requestNodeInfo(Object node) {
        return null;
    }
    /* RequestData End */


    public void test() {

        //拿到分类列表（一级）
        {
            new Thread(new Runnable() {
                @Override
                public void run() {

//                    final CategoryList categoryList = requestCategoryList();
//                    XLog.d(TAG, "requestCategoryList ret=" + categoryList);
//                    if (categoryList != null && categoryList.getCategories() != null && categoryList.getCategories().size() > 0) {
//                        final TagList tagList = requestTagList(categoryList.getCategories().get(0));
//                        XLog.d(TAG, "requestTagList ret=" + tagList);
//                        if (tagList != null && tagList.getTagList() != null && tagList.getTagList().size() > 0) {
//                            final AlbumList albumList = requestAlbumList(tagList.getTagList().get(0), -1);
//                            XLog.d(TAG, "requestAlbumList ret=" + albumList);
//                            if (albumList != null && albumList.getAlbums() != null && albumList.getAlbums().size() > 0) {
//                                final TrackList trackList = requestTrackList(albumList.getAlbums().get(0), -1);
//                                XLog.d(TAG, "requestTrackList ret=" + trackList);
//                                if (trackList != null && trackList.getTracks() != null && trackList.getTracks().size() > 0) {
//                                    XLog.d(TAG, TAG);
//                                }
//                            }
//                        }
//                    }

                }
            }).start();
        }

        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JsonNodeListResult categories = requestNodeList(null, -1);
                    XLog.d(TAG, TAG);
                    JsonNodeListResult tags = requestNodeList(categories.items.get(1), -1);
                    XLog.d(TAG, TAG);
                    JsonNodeListResult albums = requestNodeList(tags.items.get(1), -1);
                    XLog.d(TAG, TAG);
                    JsonNodeListResult tracks = requestNodeList(albums.items.get(1), -1);
                    XLog.d(TAG, TAG);
                }
            }).start();
        }

        //拿到标签列表（二级）
        {
//            Map<String, String> map = new HashMap<>();
//            map.put(DTransferConstants.CATEGORY_ID, "3"); //有声书
//            map.put(DTransferConstants.TYPE, "0");
//            CommonRequest.getTags(map, new IDataCallBack<TagList>() {
//                @Override
//                public void onSuccess(TagList tagList) {
//                    Log.d("", "");
//                }
//                @Override
//                public void onError(int i, String s) {
//                }
//            });
        }

        //拿到专辑列表（三级）
        {
//            Map<String ,String> map = new HashMap<>();
//            map.put(DTransferConstants.CATEGORY_ID ,"3");  //有声书
//            map.put(DTransferConstants.CALC_DIMENSION ,"1");
//            map.put(DTransferConstants.TAG_NAME ,"言情");
//            CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>() {
//                @Override
//                public void onSuccess(AlbumList albumList) {
//                    Log.d("", "");
//                }
//                @Override
//                public void onError(int i, String s) {
//                }
//            });
        }

        //拿到声音列表（四级）
        {
//            Map<String, String> map = new HashMap<>();
//            map.put(DTransferConstants.ALBUM_ID, "3796637"); //神医嫡女（有声书-言情）
//            CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
//                @Override
//                public void onSuccess(TrackList trackList) {
//                    Log.d("", "");
//                    //播放声音（五级）
//                    mPlayerManager.playList(trackList.getTracks(), 1); //第2章 刮骨疗伤 （有声书-言情-神医嫡女）
//                }
//                @Override
//                public void onError(int i, String s) {
//                }
//            });
        }

    }

}
