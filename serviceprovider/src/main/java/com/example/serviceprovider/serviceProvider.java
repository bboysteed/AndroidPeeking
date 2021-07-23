package com.example.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class serviceProvider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);

        Intent aidlIntent = new Intent(this, MyService.class);
        aidlIntent.setAction("com.example.serviceprovider.myservice");
        this.startService(aidlIntent);
    }
}