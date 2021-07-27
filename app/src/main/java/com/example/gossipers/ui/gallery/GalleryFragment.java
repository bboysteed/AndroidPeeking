package com.example.gossipers.ui.gallery;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.widget.TableRow;
import android.widget.Toast;

import com.example.gossipers.R;
import com.example.gossipers.ui.gallery.GalleryViewModel;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryFragment extends Fragment{
    private View root;
//    private GalleryViewModel galleryViewModel;

    // params for tables
    private DataCollector dataCollector;
    private boolean overThread;
    private final int TxtSize = 15;
    private final int HeadTxtSize = 20;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;

    // array for apps
    public static ArrayList<String[]> AppList = new ArrayList<>();

    // variable and obj for refresh UI
    private boolean Exit = false;
    private final int RefreshInterval = 1000;  // ms

    // use handler to handle msg to refresh UI in thread
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setTable();
        }
    };

    // socket should not be set up in main thread
    private final Thread Refresh = new Thread(new Runnable() {
        public void run() {
            try {
                // only create one socket
                dataCollector = new DataCollector(6666, "127.0.0.1");
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(!Exit) {
                try {
                    getData();
                    mHandler.sendMessage(mHandler.obtainMessage());
                    Thread.sleep(RefreshInterval);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    // once fragment creates view, get apps' data and create table and use thread to refresh UI
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        galleryViewModel =
//                ViewModelProviders.of(this).get(GalleryViewModel.class);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);
        // set up connection to collect data
        Refresh.start();  // use a thread to refresh UI
        return root;
    }

    // get data from socket
    public void getData() throws IOException, InterruptedException {
        AppList.clear();
        dataCollector.recvData();
//        genLocalData();
        sortData();
    }

    // generate data locally
    public void genLocalData(){
        for (int i = 0; i < (int) (Math.random() * 35); i++) {
            String[] data = new String[4];
            data[0] = "com.example.wwd666";
            data[1] = Integer.toString((int) (Math.random() * 10000));
            data[2] = Integer.toString((int) (Math.random() * 100));
            data[3] = Integer.toString((int) (Math.random() * 100));
            AppList.add(data);
        }
    }

    // sort by app's CPU usage
    public void sortData(){
        // bubble sort
        for (int i = 0; i < AppList.size(); i++){
            for (int j = 0; j < AppList.size() - i - 1; j++){
                String[] firstString = AppList.get(j);
                String[] secondString = AppList.get(j + 1);
                int firstCPU = Integer.parseInt(firstString[2]);
                int secondCPU = Integer.parseInt(secondString[2]);
                if (firstCPU < secondCPU) {
                    AppList.set(j, secondString);
                    AppList.set(j + 1, firstString);
                }
            }
        }
    }

    // set head columns
    public void setHeadColumns(TableLayout headTableLayout) {
        String[] headColumns = {"APP Name", "PID", "%CPU", "%MEM"};
        // add head row
        TableRow tableRow = new TableRow(this.getContext());
        // add head columns
        for (String name : headColumns){
            TextView tv = new TextView(this.getContext());
            tv.setText(name);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(HeadTxtSize);
            tv.setTextColor(Color.parseColor("#008577"));
            // add column to head row
            tableRow.addView(tv);
        }
        //新建的TableRow添加到TableLayout
        headTableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC,10));
    }

    // set table
    public void setTable() {
        TableLayout tableLayout;
        if(!AppList.isEmpty()){
            // 获取控件tableLayout
            tableLayout = (TableLayout)root.findViewById(R.id.table);
            // 清除表格所有行
            tableLayout.removeAllViews();
            // 全部列自动填充空白处
            tableLayout.setStretchAllColumns(true);
            // set head columns
            setHeadColumns(tableLayout);
            // 生成appNums行，4列的表格
            for(String[] app : AppList) {
                TableRow tableRow = new TableRow(this.getContext());
                // judge if cpu usage is over limit
                overThread = Integer.parseInt(app[2]) >= 80;
                for(String data : app) {
                    // tv用于显示
                    TextView tv = new TextView(this.getContext());
                    tv.setText(data);
                    tv.setTextSize(TxtSize);
                    tv.setGravity(Gravity.CENTER);
                    // if data status is fine, set color to green
                    tv.setTextColor(Color.parseColor("#008577"));
                    // else to red
                    if (overThread)
                        tv.setTextColor(Color.parseColor("#E3170D"));
                    tableRow.addView(tv);
                }
                // 新建的TableRow添加到TableLayout
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC,1));
            }
        }else{
            Toast.makeText(getActivity(),"No data received",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart(){
        Exit = false;           // start refreshing
        super.onStart();
    }

    @Override
    public void onPause() {
        Exit = true;            // stop refreshing
        Refresh.interrupt();    // stop refreshing
        super.onPause();
    }

}

