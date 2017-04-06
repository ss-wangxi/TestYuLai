package cc.snser.test.yulai.xmly;

import android.util.Log;
import android.widget.Toast;

import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.HashMap;
import java.util.Map;

import cc.snser.test.yulai.App;
import cc.snser.test.yulai.util.XLog;

/**
 * Created by wangxi-xy on 2017/4/6.
 */

public class XmlyController implements IXmPlayerStatusListener {
    private static final String TAG = "XmlyController";

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
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
    }

    @Override
    public void onBufferingStart() {
    }

    @Override
    public void onBufferingStop() {
    }

    @Override
    public void onBufferProgress(int i) {
    }

    @Override
    public void onPlayProgress(int i, int i1) {
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }
    /* IXmPlayerStatusListener End */


    public void test() {

        //拿到分类列表（一级）
        {
//            CommonRequest.getCategories(null, new IDataCallBack<CategoryList>() {
//                @Override
//                public void onSuccess(CategoryList object) {
//                    Log.d("", "");
//                }
//                @Override
//                public void onError(int code, String message) {
//                }
//            });
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
            Map<String, String> map = new HashMap<>();
            map.put(DTransferConstants.ALBUM_ID, "3796637"); //神医嫡女（有声书-言情）
            CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
                @Override
                public void onSuccess(TrackList trackList) {
                    Log.d("", "");
                    //播放声音（五级）
                    mPlayerManager.playList(trackList.getTracks(), 1); //第2章 刮骨疗伤 （有声书-言情-神医嫡女）
                }
                @Override
                public void onError(int i, String s) {
                }
            });
        }

    }

}
