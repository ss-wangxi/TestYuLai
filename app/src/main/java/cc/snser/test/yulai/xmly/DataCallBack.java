package cc.snser.test.yulai.xmly;

import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cc.snser.test.yulai.util.XLog;

/**
 * Created by wangxi-xy on 2017/4/7.
 * <p/>IDataCallBack的实现类，同时通过锁的机制实现了同步回调
 */

public class DataCallBack<T> implements IDataCallBack<T> {
    private final Lock mLock = new ReentrantLock();
    private final Condition mCondition = mLock.newCondition();

    private T mData;

    public void lock() {
        mLock.lock();
    }

    public void unlock() {
        mLock.unlock();
    }

    public boolean waitCallback(long timeoutMillis) {
        boolean succ = false;
        try {
            succ = mCondition.await(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return succ;
    }

    private void signalAll() {
        mCondition.signalAll();
    }

    public T getData() {
        return mData;
    }

    @Override
    public void onSuccess(T data) {
        XLog.d(XmlyController.TAG, "DataCallBack onSuccess " + data.getClass().getSimpleName());
        lock();
        try {
            mData = data;
        } finally {
            signalAll();
            unlock();
        }
    }

    @Override
    public void onError(int errCode, String errMsg) {
        XLog.d(XmlyController.TAG, "DataCallBack onError errCode=" + errCode + " errMsg=" + errMsg);
        lock();
        try {
            mData = null;
        } finally {
            signalAll();
            unlock();
        }
    }
}
