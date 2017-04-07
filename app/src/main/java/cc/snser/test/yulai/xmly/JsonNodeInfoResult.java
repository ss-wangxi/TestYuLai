package cc.snser.test.yulai.xmly;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Snser on 2017/1/21.
 */

public class JsonNodeInfoResult {
    private static final int CODE_SUCC = 200;

    public int code;
    public String title;
    public String title_mp3;
    //public String text;
    public String text_mp3;

    public JsonNodeInfoResult() {
        this.code = CODE_SUCC;
    }

    public static JsonNodeInfoResult parse(String strNodeInfoResult) {
        JsonNodeInfoResult jsonResult = null;
        try {
            jsonResult = new GsonBuilder().serializeNulls().create().fromJson(strNodeInfoResult, JsonNodeInfoResult.class);
            if (jsonResult != null) {
                jsonResult.filter();
            }
            if (jsonResult != null && !jsonResult.checkVaild()) {
                jsonResult = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * 过滤掉items中的非法数据(可能会修改total字段)
     * @return
     */
    private void filter() {
        if (code == CODE_SUCC) {
            if (!TextUtils.isEmpty(title_mp3) && !title_mp3.startsWith(Server.HOST)) {
                title_mp3 = Server.HOST + title_mp3;
            }
            if (!TextUtils.isEmpty(text_mp3) && !text_mp3.startsWith(Server.HOST)) {
                text_mp3 = Server.HOST + text_mp3;
            }
        }
    }

    /**
     * 检查数据是否合法
     * @return
     */
    private boolean checkVaild() {
        return code == CODE_SUCC
                && !TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(title_mp3)
                && !TextUtils.isEmpty(text_mp3);
    }

    public String getPlayUrl() {
        return text_mp3;
    }

}
