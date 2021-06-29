package com.example.gossipers.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("User Guide:\n\t1.Use CHECK AIDL first to check if the app runs well" +
                "\n\t\t\t2.1.If it shows \"aidl runs well\" then continue" +
                "\n\t\t\t2.2.If it shows anything else, check if app serviceProvider is running" +
                "\n\t3.Set test params then press TEST AIDL button to start test\n");
    }

    public LiveData<String> getText() {
        return mText;
    }
}