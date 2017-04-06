package cc.snser.test.yulai;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import cc.snser.test.yulai.speech.baidu.BaiduSpeechController;

/**
 * Created by wangxi-xy on 2017/4/5.
 */

public class App extends Application {

    private static App sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        globalInit();
    }

    private void globalInit() {
        BaiduSpeechController.getInstance().init();
    }


    public static App getApp() {
        return sApplication;
    }

    public static Context getAppContext() {
        return sApplication.getApplicationContext();
    }

    public boolean isDebugMode() {
        try {
            return (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

}
