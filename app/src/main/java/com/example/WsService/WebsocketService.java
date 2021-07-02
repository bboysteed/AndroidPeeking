package com.example.WsService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;

import static java.lang.Runtime.getRuntime;

/**
 ***************************************************************
 *
 * @版权 LinFeng
 *
 * @作者 LinFeng
 *
 * @版本 1.0
 *
 * @创建日期 2016-6-8
 *
 * @功能描述
 *****************************************************************
 */
public class WebsocketService extends Service{

    /**
     * Service中唯一的抽象方法，必须在子类中实现
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 服务创建的时候调用
     */
    @Override
    public void onCreate() {
        super.onCreate();

        final Thread serverThread  = new Thread(new Runnable() {
            @Override
            public void run() {
                Process process;
                DataOutputStream os;
                try {
                    process = getRuntime().exec("sh -c cd /data/data && ./androidVer");//看情况可能是su
//                    process.waitFor();

//                    String cmd = "cd /data/data && ./androidVer";
//                    os = new DataOutputStream(process.getOutputStream());
//                    os.write(cmd.getBytes());
//                    os.writeBytes("\n");
//                    os.flush();
                } catch (Exception e) {
                    Log.i("auto", "run command process exception:" + e.toString());
                }
            }
        });
        serverThread.start();
        Toast.makeText(this, "ws服务已经启动", Toast.LENGTH_LONG).show();
                //websocket server
//        final Thread serverThread  = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Process process;
//                DataOutputStream os;
//                try {
//                    process = getRuntime().exec("sh");//看情况可能是su
//                    String cmd = "cd /data/data && ./androidVer";
//                    os = new DataOutputStream(process.getOutputStream());
//                    os.write(cmd.getBytes());
//                    os.writeBytes("\n");
//                    os.flush();
//                } catch (Exception e) {
//                    Log.i("auto", "run command process exception:" + e.toString());
//                }
//
//
////                    String str3 = new ExecCommand().run(cmd3, 1000).getResult();
////                    System.out.println(str3);
//            }
//        });
//        serverThread.start();
    }






    /**
     * 每一次服务启动的时候调用，这个案例中，每点击一次start service都会执行的方法
     * oncreate只是第一次点击的时候执行
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务销毁的时候调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}