package com.example.library3.ui.premium;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PremiumViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PremiumViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is premium fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}