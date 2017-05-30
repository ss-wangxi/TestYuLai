package cc.snser.test.yulai;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Snser on 2017/4/13.
 */

public class Test {


    private static final String css = "css";

    @SnserAnnotation(id = 5, forceLabel = css)
    public static void test001() {
    }

    private class testClass {
    }

}
