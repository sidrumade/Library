package com.example.library3.ui.free;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FreeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FreeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment"); //setting data to mText
    }

    public LiveData<String> getText() {
        return mText;
    } //passing mText
}