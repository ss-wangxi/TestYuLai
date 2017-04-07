package cc.snser.test.yulai.xmly;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cc.snser.test.yulai.util.NetworkUtils;
import cc.snser.test.yulai.util.XLog;

/**
 * Created by Snser on 2017/1/14.
 */

public class Server {
    private static final String TAG = "Server";

    public static final String HOST = "http://guangming.sdlib.com";
    private static final String URL_GET_NODE = HOST + "/api/apk/node.php";
    private static final String URL_GET_INFO = HOST + "/api/apk/info.php";
    private static final String URL_GET_TTS = HOST + "/back/tts.jsp";

    private static final int VERSION = 1;
    private static final String MID = "";//DeviceInfoUtils.getVerifyId(App.getAppContext());

    public static final int LAST_DEFAULT = -1;
    public static final int PAGE_SIZE_DEFAULT = 20;

    public static final int LIST_UNKNOWN = Integer.MIN_VALUE;
//    public static final int LIST_RECENT = RecentController.ID;
    public static final int LIST_DIRECTORY = 1;
    public static final int LIST_ARTICLE = 2;

    private static final int HTTP_TIMEOUT_MILLS = 30 * 1000;

    public static JsonNodeListResult requestNodeList(int node) {
        return requestNodeList(node, LAST_DEFAULT, PAGE_SIZE_DEFAULT);
    }

    public static JsonNodeListResult requestNodeList(int node, int last) {
        return requestNodeList(node, last, PAGE_SIZE_DEFAULT);
    }

    public static JsonNodeListResult requestNodeList(int node, int last, int length) {
//        if (node == RecentController.ID) {
//            return RecentController.getIntance().getRecentList();
//        } else {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", String.valueOf(node)));
            params.add(new BasicNameValuePair("last", String.valueOf(last)));
            params.add(new BasicNameValuePair("len", String.valueOf(length)));
            build(params);
            print(params, "requestNodeList");
            String strResult = NetworkUtils.httpGetData(URL_GET_NODE, params, HTTP_TIMEOUT_MILLS, null);
            XLog.d(TAG, "requestNodeList strResult=" + strResult);
            return JsonNodeListResult.parse(node, strResult);
//        }
    }

    public static JsonNodeInfoResult requestNodeInfo(int node) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", String.valueOf(node)));
        build(params);
        print(params, "requestNodeInfo");
        String strResult = NetworkUtils.httpGetData(URL_GET_INFO, params, HTTP_TIMEOUT_MILLS, null);
        XLog.d(TAG, "requestNodeInfo strResult=" + strResult);
        return JsonNodeInfoResult.parse(strResult);
    }

    /**
     * 加上通用参数
     * @param params
     * @return
     */
    private static List<NameValuePair> build(final List<NameValuePair> params) {
        if (params != null && params.size() > 0) {
            params.add(new BasicNameValuePair("v", String.valueOf(VERSION)));
            params.add(new BasicNameValuePair("u", MID));
        }
        return params;
    }

    /**
     * 打印参数
     * @param params
     * @param from
     */
    public static void print(final List<NameValuePair> params, final String from) {
        XLog.v(TAG, "------------- print params (from=" + from + ") begin -------------");
        for (NameValuePair param : params) {
            XLog.v(TAG, "key=" + param.getName() + " value=" + param.getValue());
        }
        XLog.v(TAG, "------------- print params (from=" + from + ") end -------------");
    }



    public static String requestTTSUrl(String text) {
        String ttsUrl = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("text", text));
        String strResult = NetworkUtils.httpGetData(URL_GET_TTS, params, HTTP_TIMEOUT_MILLS, null);
        try {
            ttsUrl = strResult.split("\r\n").clone()[1].replace("path=", "");
        } catch (Exception e) {
        }
        if (ttsUrl != null) {
            ttsUrl = Server.HOST + ttsUrl;
        }
        return ttsUrl;
    }

    public static void autoDownloadMp3(final String text, final boolean storeTextName) {
        String storePath = "/sdcard/snser/tts/download/";
        if (storeTextName) {
            storePath += (text + ".mp3");
            if (new File(storePath).exists()) {
                XLog.d("AutoSpy", "autoDownloadMp3 text=" + text + " storePath=" + storePath + " ignore");
                return;
            }
        }

        final String url = requestTTSUrl(text);
        final String urlFileName = url != null ? url.substring(url.lastIndexOf("/") + 1) : "";
        if (!storeTextName) {
            storePath += urlFileName;
        }

        final boolean succ = downloadFile(url, storePath);
        XLog.d("AutoSpy", "autoDownloadMp3 text=" + text + " storePath=" + storePath + " succ=" + succ);

        // XLog.d("AutoSpy", "autoDownloadMp3 url=" + url);
    }

    private static boolean downloadFile(String serverLink, String storePath){
        final String serverLinkInter = serverLink;
        final String storePathInter = storePath;
        try {
            boolean fAlreadyDownloaded = false;
            File file = new File(storePathInter);
            if (file.exists()){
                fAlreadyDownloaded = true;
            }
            URLConnection conn = null;
            InputStream inStream = null;
            int filesize = 0;

            if (!fAlreadyDownloaded){
                conn = new URL(serverLinkInter).openConnection();
                conn.connect();
                inStream = conn.getInputStream();
                filesize = conn.getContentLength();
            }

            if (filesize > 0 && inStream != null){
                FileOutputStream foutStream = new FileOutputStream(storePathInter);
                byte buf[] = new byte[1024];
                while (true){
                    int readSize = inStream.read(buf);
                    if (readSize == -1)
                    {
                        break;
                    }
                    foutStream.write(buf, 0, readSize);
                }
                inStream.close();
                foutStream.close();
                return true;
            } else if (fAlreadyDownloaded) {
                return true;
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return false;
    }

}
