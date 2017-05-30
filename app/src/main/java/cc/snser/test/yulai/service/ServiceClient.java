package cc.snser.test.yulai.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import cc.snser.test.yulai.App;
import cc.snser.test.yulai.util.XLog;

/**
 * Created by Snser on 2017/5/29.
 */

public class ServiceClient {

    private IRemoteService mService;

    private ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            String descriptor = null;
            try {
                descriptor = binder.getInterfaceDescriptor();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            XLog.d("RemoteServiceClient", "onServiceConnected pid=" + android.os.Process.myPid());
            XLog.d("RemoteServiceClient", "onServiceConnected binder.hashCode=" + binder.hashCode());
            XLog.d("RemoteServiceClient", "onServiceConnected binder.getInterfaceDescriptor=" + descriptor);
            XLog.d("RemoteServiceClient", "onServiceConnected clsname=" + IRemoteService.class.getName());
            XLog.d("RemoteServiceClient", "onServiceConnected queryLocalInterface=" + binder.queryLocalInterface(IRemoteService.class.getName()));
            mService = IRemoteService.Stub.asInterface(binder);
            XLog.d("RemoteServiceClient", "onServiceConnected mService.asBinder=" + mService.asBinder() + " mService.asBinder.hashCode()=" + mService.asBinder().hashCode());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private IRemoteCallback mCallback = new IRemoteCallback.Stub() {
    };

    public void init() {
        Context context = App.getAppContext();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("cc.snser.test.yulai", "cc.snser.test.yulai.service.RemoteService"));
        context.bindService(intent, mConnect, Context.BIND_AUTO_CREATE);
    }

    public void connect() {
        if (mService != null) {
            try {
                XLog.d("RemoteServiceClient", "connect pid=" + android.os.Process.myPid());
                mService.connect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCallback() {
        if (mService != null) {
            try {
                XLog.d("RemoteServiceClient", "addCallback mCallback.hashCode=" + mCallback.hashCode()
                        + " mCallback.asBinder().hashCode()=" + mCallback.asBinder().hashCode());
                mService.addCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
