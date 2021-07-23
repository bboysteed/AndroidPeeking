//WebSocketClient 和 address
package com.example.gossipers.ui.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.widget.TextView;

import com.example.gossipers.ui.home.DashBoardBatteryView;
import com.example.gossipers.ui.home.DashBoardCPuView;
import com.example.gossipers.ui.home.DashBoardRamView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;


public class WebSockets {

    private WebSocketClient mWebSocketClient;
    private String address = "ws://127.0.0.1:8089/ws";
    private float cpurate;
    private float ramrate;
//    private float temprature;
    private DashBoardCPuView dashBoardCPuView;
    private DashBoardRamView dashBoardRamView;


    public WebSockets(DashBoardCPuView dashBoardCPuView,DashBoardRamView dashBoardRamView) {
        this.dashBoardCPuView = dashBoardCPuView;
        this.dashBoardRamView = dashBoardRamView;
    }
//初始化WebSocketClient

    /**
     * @throws URISyntaxException
     */
    public void initSocketClient() throws URISyntaxException {
        if (mWebSocketClient == null) {
            mWebSocketClient = new WebSocketClient(new URI(address)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {//连接成功
                    System.out.println("opened connection");
                }
                @Override
                public void onMessage(String s) {//服务端消息
                    try {
                        JSONObject  jsonObject = new JSONObject(s);
                        System.out.println(jsonObject);
                        JSONObject cpuObj = jsonObject.getJSONObject("cpuInfo");
                        JSONObject memObj = jsonObject.getJSONObject("ramInfo");
                        cpurate = Float.parseFloat(cpuObj.getString("all_rate"));
                        ramrate = Float.parseFloat(memObj.getString("rate"));

//                        System.out.println(cpuObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    cpurate = s;
                    System.out.println("received:" + s);
                    Message msg = new Message();
                    //给message对象赋值
                    msg.what = 1;
                    //发送message值给Handler接收
                    mHandler.sendMessage(msg);

                }


                @Override
                public void onClose(int i, String s, boolean remote) {
//连接断开，remote判定是客户端断开还是服务端断开
                    System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + ", info=" + s);
                    //
                    closeConnect();
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("error:" + e);
                }
            };
        }
    }


    //连接
    public void connect() {
        Runnable socketRun = new Runnable() {
            @Override
            public void run() {
                mWebSocketClient.connect();
            }
        };
        socketRun.run();

    }
    @SuppressLint("HandlerLeak")
    private  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 更新UI
            switch (msg.what) {
                case 1:

                    if (msg != null) {
                        dashBoardCPuView.setCurrentValue(cpurate);
                        dashBoardRamView.setCurrentValue(ramrate);
                    } else {
                        System.out.println("数据错误！");
                    }
                    break;
            }


        }
    };

            //断开连接
            private void closeConnect() {
                try {
                    mWebSocketClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mWebSocketClient = null;
                }
            }


//发送消息

            /**
             *
             */
            public void sendMsg(String msg) {
                mWebSocketClient.send(msg);
            }
        }