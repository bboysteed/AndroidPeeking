package com.example.gossipers.ui.slideshow;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.serviceprovider.IService;
import com.example.gossipers.R;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class SlideshowFragment extends Fragment implements View.OnClickListener{
    private IService iService;
    private SlideshowViewModel slideshowViewModel;
    private int threadNum;
    private int loopNum;
    private int interval;
    public static ArrayList<String> testResult = new ArrayList<String>();
    public static boolean runWell = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // bind service in another app
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.example.serviceprovider.myservice");
        serviceIntent.setPackage("com.example.serviceprovider");
        context.bindService(serviceIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iService = IService.Stub.asInterface(service);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        root.findViewById(R.id.btn_check).setOnClickListener(this);
        root.findViewById(R.id.btn_test).setOnClickListener(this);
        return root;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_check:
                try {
                    Toast.makeText(getActivity(), iService.getName(), Toast.LENGTH_SHORT).show();
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                break;
            case R.id.btn_test:
                try {
                    initResult();
                    check();
                    printResult();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void initResult(){
        if (SlideshowFragment.testResult.size() != 0) {
            SlideshowFragment.testResult.clear();
        }
    }

    public void check() throws InterruptedException {
        getParams();
        for (int i = 0; i < threadNum; i++){
            new MyThread(loopNum, interval, Integer.toString(i), iService).start();
        }
        waitForComplete();
        if (!SlideshowFragment.runWell){
            Toast.makeText(getActivity(), "test failed", Toast.LENGTH_SHORT).show();;
        }else {
            Toast.makeText(getActivity(), "test success", Toast.LENGTH_SHORT).show();;
        }
        System.out.printf("AIDL TEST RESULT(false=fail, true=success): %s\n", runWell);
    }

    public void getParams(){
        EditText etThreadNum = (EditText) this.getView().findViewById (R.id.et_threadNum);
        EditText etLoopNum = (EditText) this.getView().findViewById (R.id.et_loopNum);
        EditText etInterval = (EditText) this.getView().findViewById (R.id.et_interval);

        threadNum = Integer.parseInt(etThreadNum.getText().toString());
        loopNum = Integer.parseInt(etLoopNum.getText().toString());
        interval = Integer.parseInt(etInterval.getText().toString());
    }

    public void waitForComplete() throws InterruptedException {
        while (SlideshowFragment.testResult.size() != threadNum) {
            System.out.println("waiting all threads to complete");
            Thread.sleep(1000);
        }
    }

    public void printResult() throws InterruptedException {
        String results = "Test Result:\n";
        for (String result : SlideshowFragment.testResult) {
            results += result;
        }
        TextView result = this.getView().findViewById(R.id.text_result);
        result.setText(results);
    }
}

class MyThread extends Thread{
    private static ReentrantLock lock = new ReentrantLock();
    private final int loopNum;
    private final String threadName;
    private final int interval;
    private Thread t;
    private IService iService;
    private static int anotherTestNum = 0;

    MyThread(int num, int intervalTime, String name, IService test){
        loopNum = num;
        interval = intervalTime;
        threadName = name;
        iService = test;
    }

    public void run() {
        try {
            loopCheck();
//            resCompetitionCheck();
        } catch (RemoteException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starting thread " +  threadName );
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public void loopCheck() throws RemoteException, InterruptedException {
        System.out.printf("thread %s working\n", Thread.currentThread().getName());
        for (int i = 0; i < loopNum; i++) {
            if (iService.testAIDL(i) != i + 3) {
                lock.lock();
                SlideshowFragment.runWell = false;
                SlideshowFragment.testResult.add(String.format("\tthread %s failed;\n", Thread.currentThread().getName()));
                lock.unlock();
                break;
            }
            if (i == loopNum - 1) {
                SlideshowFragment.testResult.add(String.format("\tthread %s success;\n", Thread.currentThread().getName()));
            }
            Thread.sleep(interval);
        }

        System.out.printf("thread %s done\n", Thread.currentThread().getName());
    }
//    if wanna test
//    public void resCompetitionCheck() throws RemoteException, InterruptedException {
//        for (int i = 0; i < loopNum; i++) {
//            lock.lock();
//            MyThread.anotherTestNum++;
//            System.out.printf("thread %s working on anotherTestNum\n", Thread.currentThread().getName());
//            lock.unlock();
//            System.out.printf("thread %s work done\n", Thread.currentThread().getName());
//            Thread.sleep(interval);
//        }
//
//        System.out.printf("thread %s done\n", Thread.currentThread().getName());
//    }
}
