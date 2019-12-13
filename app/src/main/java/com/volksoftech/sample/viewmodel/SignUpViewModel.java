package com.volksoftech.sample.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.volksoftech.sample.model.User;

public class SignUpViewModel extends ViewModel {
    public MutableLiveData<String> input_name = new MutableLiveData<>();
    public MutableLiveData<String> input_email = new MutableLiveData<>();
    public MutableLiveData<String> input_password = new MutableLiveData<>();
    public MutableLiveData<String> input_phoneNumber = new MutableLiveData<>();


    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }
}
