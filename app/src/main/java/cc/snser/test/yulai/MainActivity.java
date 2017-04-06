package cc.snser.test.yulai;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
                BaiduSpeechController.getInstance().test(mEdit.getText().toString());
                break;
            case R.id.btn_test_b:
                BaiduSpeechController.getInstance().stop();
                break;
            case R.id.btn_test_c:
                BaiduSpeechController.getInstance().playTts("芝麻开门");
                break;
            default:
                break;
        }
    }
}
