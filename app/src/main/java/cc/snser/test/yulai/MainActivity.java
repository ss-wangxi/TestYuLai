package cc.snser.test.yulai;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.qihoo.ai.drivermannersdk.DetectCallBack;
import com.qihoo.ai.drivermannersdk.DetectDecide;

import cc.snser.test.yulai.service.ServiceClient;
import cc.snser.test.yulai.speech.baidu.BaiduSpeechController;
import cc.snser.test.yulai.util.XLog;
import cc.snser.test.yulai.xmly.XmlyController;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdit = (EditText)findViewById(R.id.edit);
        findViewById(R.id.btn_test_a).setOnClickListener(this);
        findViewById(R.id.btn_test_b).setOnClickListener(this);
        findViewById(R.id.btn_test_c).setOnClickListener(this);

//        DetectDecide mDetectDecide = new DetectDecide(App.getAppContext());
//        mDetectDecide.registerGsensor(mDetectDecide, new DetectCallBack() {
//            @Override
//            public void DetectBrake(float values, long time) {
//                XLog.d("DetectDecide", "急刹车");
//            }
//
//            @Override
//            public void DetectAccelerate(float values, long time) {
//                XLog.d("DetectDecide", "急加速");
//            }
//
//            @Override
//            public void DetectTurn(float values, long time) {
//                XLog.d("DetectDecide", "急转弯");
//            }
//        });
    }

    ServiceClient serviceClient = new ServiceClient();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_a:
                serviceClient.init();
                break;
            case R.id.btn_test_b:
                serviceClient.connect();
                break;
            case R.id.btn_test_c:
                serviceClient.addCallback();
                break;
            default:
                break;
        }
    }
}
