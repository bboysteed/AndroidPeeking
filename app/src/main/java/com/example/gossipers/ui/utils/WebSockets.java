//WebSocketClient 和 address
package com.example.gossipers.ui.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;


public class WebSockets {

    private WebSocketClient mWebSocketClient;
    private String address = "ws://10.135.180.99:9000/chatSocket";
    private String cpurate;
    private TextView textView;

    public WebSockets(TextView target) {
        this.textView = target;
    }
//初始化WebSocketClient

    /**
     * @throws URISyntaxException
     */
    public void initSocketClient() throws URISyntaxException {
        if (mWebSocketClient == null) {
            mWebSocketClient = new WebSocketClient(new URI(address)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
//连接成功
                    System.out.println("opened connection");
                }


                @Override
                public void onMessage(String s) {//服务端消息
                    cpurate = s;
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
                        textView.setText(cpurate);
                    } else {
                        textView.setText("数据错误！");
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