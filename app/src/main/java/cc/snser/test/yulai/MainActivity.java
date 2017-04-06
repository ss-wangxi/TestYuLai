package cc.snser.test.yulai;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cc.snser.test.yulai.speech.baidu.BaiduSpeechController;
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_a:
                XmlyController.getInstance().test();
                break;
            case R.id.btn_test_b:
                break;
            case R.id.btn_test_c:
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.qihu.mobile.demo", "com.qihu.mobile.demo.MainActivity"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
