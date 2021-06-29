package com.example.gossipers;

import android.os.Bundle;

import com.example.WsService.WebsocketService;
import com.example.gossipers.ui.utils.ExecCommand;
import com.example.gossipers.ui.utils.WebSockets;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.net.URISyntaxException;

import static java.lang.Runtime.getRuntime;
//import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent startintent = new Intent(this, WebsocketService.class);
//        startService(startintent);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


//        //websocket server
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

//        String cmd3="cd /data/data && ./androidVer";
//        String str3 = new ExecCommand().run(cmd3, -1).getResult();
//        System.out.println(str3);
//        final  DashBoardView dashBoardView_cpu = findViewById(R.id.cpu_dashboard);
//        Button button_nome = findViewById(R.id.home_button);
//
//        button_nome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
