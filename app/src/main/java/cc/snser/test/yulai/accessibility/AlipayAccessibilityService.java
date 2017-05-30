package cc.snser.test.yulai.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import cc.snser.test.yulai.util.XLog;

/**
 * Created by Snser on 2017/5/19.
 */

public class AlipayAccessibilityService extends AccessibilityService {

    public static final String TAG = "AlipayAccessibilityService";

    @Override
    protected void onServiceConnected() {
        XLog.d(TAG, "onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        XLog.d(TAG, "onAccessibilityEvent event=" + event);
    }

    @Override
    public void onInterrupt() {
        XLog.d(TAG, "onInterrupt");
    }

}
