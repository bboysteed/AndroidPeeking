package com.example.gossipers.ui.home;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gossipers.R;
import com.example.gossipers.ui.utils.BatteryReceiver;
import com.example.gossipers.ui.utils.ExecCommand;
import com.example.gossipers.ui.utils.WebSockets;

import java.io.DataOutputStream;
import java.net.URISyntaxException;

import static java.lang.Runtime.getRuntime;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private WebSockets mwebSockets;
    private TextView textView;
//    private DashBoardView dashBoardView;
//    public static final String SDCARD_ROOT=Environment.getExternalStorageDirectory().getAbsolutePath();
//    public static final String AAA_PATH=SDCARD_ROOT+"/wifidog.conf";
    private BatteryReceiver receiver;
//    private float cpuUsedRate=10;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        textView = root.findViewById(R.id.text_home);
        final DashBoardCPuView dashBoardCPuView_cpu = root.findViewById(R.id.cpu_dashboard);
        final DashBoardRamView dashBoardRamView = root.findViewById(R.id.ram_dashboard);


        //websocket client
        mwebSockets = new WebSockets(dashBoardCPuView_cpu,dashBoardRamView);
        try {
            mwebSockets.initSocketClient();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mwebSockets.connect();

        final Button button = root.findViewById(R.id.home_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_LONG).show();
//                cpuUsedRate+=2.32;
//                dashBoardCPuView_cpu.setCurrentValue(cpuUsedRate);


//读取目标文件（绝对路径）指定内容“#TrustedMACList ”的那一行
                String cmd3="top -n 1 -d 1";
                String str3 = new ExecCommand().run(cmd3, 10000).getResult();
                System.out.println(str3);
                Toast.makeText(getActivity(), str3,Toast.LENGTH_SHORT).show();
            }
        });

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        DashBoardBatteryView dashBoardBatteryView = root.findViewById(R.id.battery_dashboard);
        IntentFilter filter  = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        receiver = new BatteryReceiver(dashBoardBatteryView);
        getActivity().registerReceiver(receiver, filter);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    //    public void onStart(){
//        super.onStart();
//        final DashBoardView dashBoardView = getActivity().findViewById(R.id.cpu_dashboard);
//        final Button button = getActivity().findViewById(R.id.home_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_LONG).show();
//                cpuUsedRate+=2.32;
//                dashBoardView.setCurrentValue(cpuUsedRate);
//            }
//        });
//    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        LayoutInflater inflater = getLayoutInflater();
//        View root = inflater.inflate(R.layout.fragment_home,(ViewGroup)findViewById(R.id.));
//        final Button button = getActivity().findViewById(R.id.home_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                cpuUsedRate+=1.5;
//                DashBoardView dashBoardView = v.findViewById(R.id.cpu_dashboard);
//                System.out.println(dashBoardView);
//                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_LONG).show();
//                dashBoardView.setCurrentValue(16);
//            }
//        });
//    }
}