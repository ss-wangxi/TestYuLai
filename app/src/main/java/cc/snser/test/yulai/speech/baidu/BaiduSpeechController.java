package cc.snser.test.yulai.speech.baidu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.util.ArrayList;

import cc.snser.test.yulai.App;
import cc.snser.test.yulai.util.FileUtils;
import cc.snser.test.yulai.util.XLog;

/**
 * Created by wangxi-xy on 2017/4/5.
 */

public class BaiduSpeechController implements SpeechSynthesizerListener {
    private static final String TAG = "BaiduSpeechController";

    private static final String APP_ID = "9481460";
    private static final String API_KEY = "i6SEvt6VgTCZPbDxiPUECyxT";
    private static final String SECRET_KEY = "cec7b576ae2f07ea7bd2f986da4aa168";

    private static final String DIR_SDCARD = new File(Environment.getExternalStorageDirectory(), "bdtts@snser").getAbsolutePath();
    private static final String DIR_ASSETS = "bdtts";

    // 在线引擎发音人（0:普通女声，1:普通男声，2:特别男声，3:情感男声）
    private static final String SPEAKER_FEMALE = "0";
    private static final String SPEAKER = SPEAKER_FEMALE;

    private static final String MODEL_SPEECH_FEMALE_CN = "bd_etts_speech_female.dat"; //离线引擎声学模型-女声-中文
    private static final String MODEL_SPEECH_FEMALE_EN = "bd_etts_speech_female_en.dat"; //离线引擎声学模型-女声-英文
    private static final String MODEL_SPEECH_MALE_CN = "bd_etts_speech_male.dat"; //离线引擎声学模型-男声-中文
    private static final String MODEL_SPEECH_MALE_EN = "bd_etts_speech_male_en.dat"; //离线引擎声学模型-男声-英文

    private static final String MODEL_SPEECH_CN = SPEAKER_FEMALE.equals(SPEAKER) ? MODEL_SPEECH_FEMALE_CN : MODEL_SPEECH_MALE_CN;
    private static final String MODEL_SPEECH_EN = SPEAKER_FEMALE.equals(SPEAKER) ? MODEL_SPEECH_FEMALE_EN : MODEL_SPEECH_MALE_EN;

    private static final String MODEL_TEXT_CN = "bd_etts_text.dat"; //离线引擎文本模型-中文
    private static final String MODEL_TEXT_EN = "bd_etts_text_en.dat"; //离线引擎文本模型-英文

    private boolean mInited = false;

    private SpeechSynthesizer mSpeech;

    private OnInitedListener mOnInitedListener;
    private OnCompletionListener mOnCompletionListener;

    private BaiduSpeechController() {
    }

    private static class SingletonHolder {
        static BaiduSpeechController INSTANCE = new BaiduSpeechController();
    }

    public static BaiduSpeechController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                XLog.d(TAG, "init start");
                setInited(false);
            }
            @Override
            protected Void doInBackground(Void... params) {
                copyModelData();
                initTts();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                XLog.d(TAG, "init done");
                setInited(true);
                notifyInit();
            }
        }.execute();
    }

    private void copyModelData() {
        final Context context = App.getAppContext();
        new File(DIR_SDCARD).mkdirs();
        final ArrayList<File> asssetFiles = new ArrayList<File>() {
            {
                add(new File(DIR_ASSETS, MODEL_SPEECH_FEMALE_CN));
                add(new File(DIR_ASSETS, MODEL_SPEECH_FEMALE_EN));
                add(new File(DIR_ASSETS, MODEL_SPEECH_MALE_CN));
                add(new File(DIR_ASSETS, MODEL_SPEECH_MALE_EN));
                add(new File(DIR_ASSETS, MODEL_TEXT_CN));
                add(new File(DIR_ASSETS, MODEL_TEXT_EN));
            }
        };
        for (File asssetFile : asssetFiles) {
            FileUtils.copyAssetsToSdcard(context, asssetFile.getPath(), new File(DIR_SDCARD, asssetFile.getName()).getAbsolutePath(), false);
        }
    }

    private void initTts() {
        mSpeech = SpeechSynthesizer.getInstance();
        mSpeech.setContext(App.getAppContext());
        mSpeech.setSpeechSynthesizerListener(this);
        //设置AppId和ApiKey
        mSpeech.setAppId(APP_ID);
        mSpeech.setApiKey(API_KEY, SECRET_KEY);
        //设置在线引擎发音人
        mSpeech.setParam(SpeechSynthesizer.PARAM_SPEAKER, SPEAKER);
        //设置离线引擎模型
        mSpeech.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, new File(DIR_SDCARD, MODEL_TEXT_CN).getAbsolutePath());
        mSpeech.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, new File(DIR_SDCARD, MODEL_SPEECH_CN).getAbsolutePath());
        //设置Mix模式的合成策略
        mSpeech.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
        //初始化tts
        final int initTtsRet = mSpeech.initTts(TtsMode.MIX);
        //加载离线英文资源（提供离线英文合成功能）
        final String modelTextEnPath = new File(DIR_SDCARD, MODEL_TEXT_EN).getAbsolutePath();
        final String modelSpeechEnPath = new File(DIR_SDCARD, MODEL_SPEECH_EN).getAbsolutePath();
        final int initEnModelRet = mSpeech.loadEnglishModel(modelTextEnPath, modelSpeechEnPath);
        //授权检测接口（仅调试需要查看，release不需要，因为sdk内部已经有校验逻辑了）
        if (App.getApp().isDebugMode()) {
            final AuthInfo authInfo = mSpeech.auth(TtsMode.MIX);
            if (authInfo.isSuccess()) {
                XLog.w(TAG, "initTts auth success");
            } else {
                String errorMsg = authInfo.getTtsError().getDetailMessage();
                XLog.w(TAG, "initTts auth failed errorMsg=" + errorMsg);
            }
        }
        XLog.w(TAG, "initTts initTtsRet=" + initTtsRet + " initEnModelRet=" + initEnModelRet);
    }


    /* SpeechSynthesizerListener begin */
    @Override
    public void onSynthesizeStart(String s) {
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
    }

    @Override
    public void onSynthesizeFinish(String s) {
    }

    @Override
    public void onSpeechStart(String s) {
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
    }

    @Override
    public void onSpeechFinish(String s) {
        notifyCompletion();
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        notifyCompletion();
    }
    /* SpeechSynthesizerListener end */


    public boolean playTts(String text) {
        boolean succ = false;
        if (isInited() && !TextUtils.isEmpty(text)) {
            succ = (mSpeech.speak(text) == 0);
        }
        return succ;
    }

    public boolean stop() {
        boolean succ = false;
        if (isInited()) {
            succ = (mSpeech.stop() == 0);
            notifyCompletion();
        }
        return succ;
    }


    public interface OnInitedListener {
        void onBaiduSpeechInited();
    }

    public void setOnInitedListener(OnInitedListener listener) {
        mOnInitedListener = listener;
    }

    private void notifyInit() {
        XLog.d(TAG, "notifyInit");
        if (mOnInitedListener != null) {
            mOnInitedListener.onBaiduSpeechInited();
        }
    }

    private synchronized void setInited(boolean inited) {
        mInited = inited;
    }

    public synchronized boolean isInited() {
        return mInited;
    }


    public interface OnCompletionListener {
        void onCompletion();
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        mOnCompletionListener = listener;
    }

    private void notifyCompletion() {
        XLog.d(TAG, "notifyCompletion");
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion();
        }
    }


    public void test(String text) {
        int ret = mSpeech.speak(!TextUtils.isEmpty(text) ? text : "你好巴迪");
        XLog.d(TAG, "test ret=" + ret);
    }

}
