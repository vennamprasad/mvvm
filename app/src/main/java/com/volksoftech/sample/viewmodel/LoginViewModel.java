package com.volksoftech.sample.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.volksoftech.sample.model.User;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> loginId = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {
        User user = new User(loginId.getValue(), password.getValue());
        userMutableLiveData.setValue(user);

    }
}
