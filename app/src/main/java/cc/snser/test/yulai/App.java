package cc.snser.test.yulai;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import java.util.List;

import cc.snser.test.yulai.speech.baidu.BaiduSpeechController;
import cc.snser.test.yulai.xmly.XmlyController;

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
        //只在主进程初始化一次
        if (getProgressPid(getAppContext(), getAppContext().getPackageName()) == android.os.Process.myPid()) {
            BaiduSpeechController.getInstance().init();
            XmlyController.getInstance().init();
        }
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

    public static int getProgressPid(Context context, String progressName) {
        if (!TextUtils.isEmpty(progressName)) {
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo process : processes) {
                if (progressName.equals(process.processName) && process.pkgList != null) {
                    return process.pid;
                }
            }
        }
        return -1;
    }

}
