package cc.snser.test.yulai;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test_a).setOnClickListener(this);
        findViewById(R.id.btn_test_b).setOnClickListener(this);
        findViewById(R.id.btn_test_c).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_a:
                BaiduSpeechController.getInstance().test();
                break;
            case R.id.btn_test_b:
                break;
            case R.id.btn_test_c:
                break;
            default:
                break;
        }
    }
}
