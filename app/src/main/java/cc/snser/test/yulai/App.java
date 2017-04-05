package cc.snser.test.yulai;

import android.app.Application;
import android.content.Context;

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

    public static App getApp() {
        return sApplication;
    }

    public static Context getAppContext() {
        return sApplication.getApplicationContext();
    }

    private void globalInit() {
        BaiduSpeechController.getInstance().init();
    }

}
