package com.example.gossipers.ui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.gossipers.ui.home.DashBoardBatteryView;

public class BatteryReceiver extends BroadcastReceiver {

    private DashBoardBatteryView dashBoardBatteryView;

    public BatteryReceiver(DashBoardBatteryView dashBoardBatteryView) {
        this.dashBoardBatteryView = dashBoardBatteryView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int current = intent.getExtras().getInt("level");// 获得当前电量
        int total = intent.getExtras().getInt("scale");// 获得总电量
        int percent = current * 100 / total;
        dashBoardBatteryView.setCurrentValue((float)percent);
    }
}
