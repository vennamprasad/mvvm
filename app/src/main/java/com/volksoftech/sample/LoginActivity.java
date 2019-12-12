package com.volksoftech.sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.volksoftech.sample.databinding.ActivityLoginBinding;
import com.volksoftech.sample.dump.RecyclerViewWithImageCapture;
import com.volksoftech.sample.viewmodel.LoginViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginViewModel);

        loginViewModel.getUser().observe(this, loginUser -> {

            if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getLoginId())) {
                binding.eloginId.setError("Enter an E-Mail Address");
                binding.eloginId.requestFocus();
            } else if (!loginUser.isEmailValid()) {
                binding.eloginId.setError("Enter a Valid E-mail Address");
                binding.eloginId.requestFocus();
            } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPassword())) {
                binding.ePassword.setError("Enter a Password");
                binding.ePassword.requestFocus();
            } else if (!loginUser.isPasswordLengthGreaterThan5()) {
                binding.ePassword.setError("Enter at least 6 Digit password");
                binding.ePassword.requestFocus();
            } else {
                startActivity(new Intent(this, RecyclerViewWithImageCapture.class));
            }

        });
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivityStudent
        moveTaskToBack(true);
    }


}
