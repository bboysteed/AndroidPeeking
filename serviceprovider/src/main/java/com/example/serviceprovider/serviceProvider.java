package com.example.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class serviceProvider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);

        Intent monitorIntent = new Intent(this, MonitorService.class);
        monitorIntent.setAction("com.example.serviceprovider.monitorservice");
        this.startService(monitorIntent);
    }
}