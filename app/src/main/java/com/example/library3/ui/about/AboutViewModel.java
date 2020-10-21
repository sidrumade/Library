package com.example.library3.ui.about;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> Devname;
    private MutableLiveData<String> DevEmail;
    private MutableLiveData<String> DevContact;

    public MutableLiveData<String> getDevname() {
        return Devname;
    }

    public void setDevname(MutableLiveData<String> devname) {
        Devname = devname;
    }

    public MutableLiveData<String> getDevEmail() {
        return DevEmail;
    }

    public void setDevEmail(MutableLiveData<String> devEmail) {
        DevEmail = devEmail;
    }

    public MutableLiveData<String> getDevContact() {
        return DevContact;
    }

    public void setDevContact(MutableLiveData<String> devContact) {
        DevContact = devContact;
    }

    public AboutViewModel() {
        Devname = new MutableLiveData<>();
        DevEmail = new MutableLiveData<>();
        DevContact = new MutableLiveData<>();

        Devname.setValue("Developer: Mayur Nagesh Satam");
        DevEmail.setValue("Email: mayursatam6327@gmail.com");
        DevContact.setValue("Contact: 919892320172");
    }
}