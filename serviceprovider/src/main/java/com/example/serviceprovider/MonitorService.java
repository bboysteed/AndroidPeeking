package com.example.serviceprovider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

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
        try {
            runService();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public void runService() throws IOException {
//        String cmdStrChgDir = "sh -c cd '/data/data/com.example.serviceprovider/Monitor/'";
        String cmdStrStartSrv = "sh -c './_androidVer'";
        Runtime run = Runtime.getRuntime();
        Process process = run.exec(cmdStrStartSrv, null, new File("data/data/com.example.serviceprovider/Monitor/"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = process.getErrorStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(reader);
                    StringBuffer sb = new StringBuffer();
                    String message;
                    for (int i = 0; i < 100; i++) {
                        System.out.println("reading");
                        message = br.readLine();
                        sb.append(message);
                    }
                    System.out.println("cmd output:" + sb);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
