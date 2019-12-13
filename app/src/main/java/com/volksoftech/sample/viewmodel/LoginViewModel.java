package com.volksoftech.sample.viewmodel;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.volksoftech.sample.R;
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
        if (view.getId() == R.id.btn_login) {
            User user = new User(loginId.getValue(), password.getValue());
            userMutableLiveData.setValue(user);
        }
    }

    public void startActivity(AppCompatActivity activity, Class<?> toClass, boolean finishReq) {
        activity.startActivity(new Intent(activity, toClass));
        if (finishReq)
            activity.finish();
    }
}
