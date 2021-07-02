package com.example.serviceprovider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MonitorService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int retVal = super.onStartCommand(intent, flags, startId);
        System.out.printf("System Monitor Service Online, retVal=%s\n", retVal);
        runService();

        return retVal;
    }

    public void runService(){
        String cmdStrStartSrv = "cd /data/data/com.example.serviceprovider/Monitor && ./_androidVer";
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmdStrStartSrv);
            InputStream in = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String message;
            while((message = br.readLine()) != null) {
                sb.append(message);
            }
            System.out.println("cmd output:" + sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
