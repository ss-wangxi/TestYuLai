package cc.snser.test.yulai.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import cc.snser.test.yulai.util.XLog;

/**
 * Created by Snser on 2017/5/29.
 */

public class RemoteService extends Service {

    private Binder mBinder = new IRemoteService.Stub() {
        @Override
        public void connect() throws RemoteException {
            XLog.d("RemoteService", "mBinder connect() pid=" + android.os.Process.myPid() + " CallingPid=" + getCallingPid());
        }

        @Override
        public void addCallback(IRemoteCallback callback) throws RemoteException {
            XLog.d("RemoteService", "mBinder addCallback() callback.hashCode=" + callback.hashCode()
                    + " callback.asBinder().hashCode()=" + callback.asBinder().hashCode());
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            XLog.d("RemoteService", "mBinder onTransact() pid=" + android.os.Process.myPid() + " CallingPid=" + getCallingPid());
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public void attachInterface(IInterface owner, String descriptor) {
            XLog.d("RemoteService", "mBinder attachInterface() pid=" + android.os.Process.myPid() + " CallingPid=" + getCallingPid());
            super.attachInterface(owner, descriptor);
        }

        @Override
        public IInterface queryLocalInterface(String descriptor) {
            XLog.d("RemoteService", "mBinder queryLocalInterface() pid=" + android.os.Process.myPid() + " CallingPid=" + getCallingPid());
            return super.queryLocalInterface(descriptor);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.d("RemoteService", "onCreate mBinder.hashCode=" + mBinder.hashCode());
        //XLog.d("RemoteService", "onCreate mBinder.hashCode=" + mBinder.);
        XLog.d("RemoteService", "onCreate mBinder.getInterfaceDescriptor=" + mBinder.getInterfaceDescriptor());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
