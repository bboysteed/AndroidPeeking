 package com.example.gossipers.ui.gallery;

import java.io.*;
import java.net.Socket;

public class DataCollector {
    private final Socket Client;
    private final OutputStream Os;
    private final InputStream Is;
    private static final String MsgExit = "exit";
    private static final String MsgACK = "ack";
    private static final String MsgPull = "pull";

    DataCollector(int p, String ip) throws IOException {
        Client = new Socket(ip, p);
        Os = Client.getOutputStream();
        Is = Client.getInputStream();
    }

    /*
    1. client send MsgPull
    2. server recv MsgPull, send MsgACK && Data
    3. client recv MsgACK, starting receiving DATA
     */
    public void recvData() throws IOException {
        int i = 0;  // index for adding string array "data" to arrayList "apps"
        String[] app = new String[4]; // to store data for each app

        // create outputStream buffer, send MsgPull to request apps' data from server
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Os));
        System.out.printf("sending msg to server: %s\n", MsgPull);
        out.write(MsgPull);
        out.flush();

        // create inputStream buffer, recv data from server
        BufferedReader in = new BufferedReader(new InputStreamReader(Is));
        while (!in.ready())
            System.out.println("BufferReader in is empty, waiting");
        String symbol = in.readLine().trim();
        if (symbol.equals(MsgACK)){
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
            System.out.printf("ACK not received, receive:%s\n", symbol);
        System.out.println("transmission complete");

//        checkData();
    }

    public void checkData() {
        for (String[] eachApp : GalleryFragment.AppList){
            for (String data : eachApp)
                System.out.println(data);
        }
    }

}
