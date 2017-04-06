package cc.snser.test.yulai.xmly;

import android.widget.Toast;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

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
    }

}
