package com.example.serviceprovider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("system bind");
        return new MyBinder();
    }

    class MyBinder extends IService.Stub {
        @Override
        public String getName() throws RemoteException {
            System.out.println("testing getName");
            return "aidl runs well";
        }

        @Override
        public int testAIDL(int num) {
            return num + 3;
        }
    }
}
