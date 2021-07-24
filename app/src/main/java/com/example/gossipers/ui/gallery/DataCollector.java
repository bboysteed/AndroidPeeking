 package com.example.gossipers.ui.gallery;

import java.io.*;
import java.net.Socket;

public class DataCollector {
    private Socket client;
    private final InputStream Is;
    private final OutputStream Os;
    private static final String MsgExit = "exit";
    private static final String MsgACK = "ack";
    private static final String MsgRequest = "pull";

    DataCollector(int p, String ip) throws IOException {
        Socket client = new Socket(ip, p);
        Is = client.getInputStream();
        Os = client.getOutputStream();
    }

    public void recvData() throws IOException {
        int i = 0;  // index for adding string array "data" to arrayList "apps"
        String[] app = new String[4]; // to store data for each app

        // create outputStream buffer, send MsgRequest to request apps' data from server
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Os));
        System.out.printf("sending msg to server: %s\n", MsgRequest);
        out.write(MsgRequest);
        out.flush();

        // create inputStream buffer, recv data from server
        BufferedReader in = new BufferedReader(new InputStreamReader(Is));
        String tempTest = in.readLine().trim();
        System.out.printf("ACK :%s\n", tempTest);
        if (tempTest.equals(MsgACK)){
            System.out.println("Client received ACK");  // log
            while(true){
                i = i % 4;  // index for data in app
                String msg = in.readLine().trim();

                if (msg.equals(MsgExit))
                    break;

                app[i] = msg;
                // store app's data in to Apps by deep copy, next app's data will cover the original
                if (i == 3){
                    String[] temp = new String[4];
                    System.arraycopy(app, 0,  temp, 0, 4);
                    GalleryFragment.AppList.add(temp);
                }

                i++;
            }
        }else
            System.out.printf("ACK not received, receive:%s\n", tempTest);
        System.out.println("socket connection close");

        checkData();

    }

    public void checkData() {
        for (String[] eachApp : GalleryFragment.AppList){
            for (String data : eachApp)
                System.out.println(data);
        }
    }

}
