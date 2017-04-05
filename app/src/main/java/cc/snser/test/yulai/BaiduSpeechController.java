package cc.snser.test.yulai;

import android.content.Context;
import android.os.Environment;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.util.ArrayList;

import cc.snser.test.yulai.util.FileUtils;
import cc.snser.test.yulai.util.XLog;

/**
 * Created by wangxi-xy on 2017/4/5.
 */

public class BaiduSpeechController implements SpeechSynthesizerListener {
    private static final String TAG = "BaiduSpeechController";

    private static final String SAMPLE_DIR_NAME = "BaiduTTS@Snser";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    private SpeechSynthesizer mSpeech;


    private BaiduSpeechController() {
    }

    private static class SingletonHolder {
        static BaiduSpeechController INSTANCE = new BaiduSpeechController();
    }

    public static BaiduSpeechController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        copyData();
        initialTts();
    }

    private void copyData() {
        final Context context = App.getAppContext();
        final String SDCARD_DIR_ROOT = Environment.getExternalStorageDirectory().toString() + File.separator + "bdtts@snser";
        new File(SDCARD_DIR_ROOT).mkdirs();
        final ArrayList<File> asssetFileNames = new ArrayList<File>() {
            {
                add(new File("bdtts/bd_etts_speech_female.dat"));
                add(new File("bdtts/bd_etts_speech_male.dat"));
                add(new File("bdtts/bd_etts_text.dat"));
                add(new File("bdtts/bd_etts_speech_female_en.dat"));
                add(new File("bdtts/bd_etts_speech_male_en.dat"));
                add(new File("bdtts/bd_etts_text_en.dat"));
            }
        };
        for (File asssetFileName : asssetFileNames) {
            FileUtils.copyAssetsToSdcard(context, asssetFileName.getPath(), SDCARD_DIR_ROOT + File.separator + asssetFileName.getName(), false);
        }
    }

    private void initialTts() {
        final String SDCARD_DIR_ROOT = Environment.getExternalStorageDirectory().toString() + File.separator + "bdtts@snser";

        mSpeech = SpeechSynthesizer.getInstance();
        mSpeech.setContext(App.getAppContext());
        mSpeech.setSpeechSynthesizerListener(this);

//        // 文本模型文件路径 (离线引擎使用)
//        mSpeech.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, "/sdcard/bdtts@Snser" + "/" + TEXT_MODEL_NAME);
//        // 声学模型文件路径 (离线引擎使用)
//        mSpeech.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, "/sdcard/bdtts@Snser" + "/" + SPEECH_FEMALE_MODEL_NAME);

        //设置AppId和ApiKey
        mSpeech.setAppId("9481460");
        mSpeech.setApiKey("i6SEvt6VgTCZPbDxiPUECyxT", "cec7b576ae2f07ea7bd2f986da4aa168");

        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        mSpeech.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");

        // 设置Mix模式的合成策略
        mSpeech.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);

        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        AuthInfo authInfo = mSpeech.auth(TtsMode.MIX);

        if (authInfo.isSuccess()) {
            XLog.d(TAG, "auth success");
        } else {
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            XLog.d(TAG, "auth failed errorMsg=" + errorMsg);
        }

        // 初始化tts
        int ret = mSpeech.initTts(TtsMode.MIX);
        XLog.d(TAG, "inittts ret=" + ret);

        // 加载离线英文资源（提供离线英文合成功能）
//        int result = mSpeech.loadEnglishModel(SDCARD_DIR_ROOT + "/" + ENGLISH_TEXT_MODEL_NAME, SDCARD_DIR_ROOT + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
//        XLog.d(TAG, "loadEnglishModel result=" + result);
    }


    /* SpeechSynthesizerListener begin */
    @Override
    public void onSynthesizeStart(String s) {
        XLog.d(TAG, "onSynthesizeStart");
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
        XLog.d(TAG, "onSynthesizeDataArrived");
    }

    @Override
    public void onSynthesizeFinish(String s) {
        XLog.d(TAG, "onSynthesizeFinish");
    }

    @Override
    public void onSpeechStart(String s) {
        XLog.d(TAG, "onSpeechStart");
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        XLog.d(TAG, "onSpeechProgressChanged");
    }

    @Override
    public void onSpeechFinish(String s) {
        XLog.d(TAG, "onSpeechFinish");
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        XLog.d(TAG, "onError");
    }
    /* SpeechSynthesizerListener end */


    public void test() {
        int ret = mSpeech.speak("123");
        XLog.d(TAG, "test ret=" + ret);
    }

}
