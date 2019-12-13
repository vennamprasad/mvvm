package com.volksoftech.sample.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.volksoftech.sample.R;
import com.volksoftech.sample.databinding.ActivitySignUpBinding;
import com.volksoftech.sample.viewmodel.SignUpViewModel;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    int step = 1;
    //name strings
    String lastName = "";
    String firstName = "";
    String emailst = "";
    String passst = "";
    String namest = "";
    String phonenumber = "";
    ActivitySignUpBinding binding = null;
    SignUpViewModel signUpViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SignUpActivity.this, R.layout.activity_sign_up);
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setSignUpViewModel(signUpViewModel);
        hideall();
        binding.namell.setVisibility(View.VISIBLE);
        /*Setting on click listeners*/
        binding.backbtn.setOnClickListener(v -> onBackPressed());
        binding.verificationcodebtn.setOnClickListener(v -> verifyCode());
        binding.btnSignup.setOnClickListener(v -> {
            if (step == 1) {
                namest = binding.inputName.getText().toString();
                if (namest.isEmpty() || checkValidName(namest)) {
                    if (namest.isEmpty()) {
                        Toast.makeText(SignUpActivity.this, "Please write your name!", Toast.LENGTH_SHORT).show();
                    } else if (checkValidName(namest)) {
                        Toast.makeText(SignUpActivity.this, "Please add full name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.namell.setVisibility(View.GONE);
                    binding.emailll.setVisibility(View.VISIBLE);
                    binding.titletxt.setText("Choose email");
                    binding.desctxt.setText("enter your email address below.");
                    step++;
                }
            } else if (step == 2) {
                emailst = binding.inputEmail.getText().toString();
                if (emailst.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please write your email!", Toast.LENGTH_SHORT).show();
                } else {
                    binding.emailll.setVisibility(View.GONE);
                    binding.passwordll.setVisibility(View.VISIBLE);
                    binding.titletxt.setText("Create Password");
                    binding.desctxt.setText("Your password must have atleast one symbol & 4 or more characters.");
                    step++;
                }

            } else if (step == 3) {
                passst = binding.inputPassword.getText().toString();
                if (!passst.isEmpty()) {
                    binding.passwordll.setVisibility(View.GONE);
                    binding.phonenumberll.setVisibility(View.VISIBLE);
                    binding.phonenumbertextll.setVisibility(View.VISIBLE);
                    binding.titletxt.setText("Let's Get Started");
                    binding.desctxt.setText("Enter your mobile number to enable 2-step verification.");
                    step++;
                }
            } else if (step == 4) {
                phonenumber = binding.inputPassword.getText().toString();
                if (!phonenumber.isEmpty()) {
                    binding.phonenumberll.setVisibility(View.GONE);
                    binding.phonenumbertextll.setVisibility(View.GONE);
                    binding.titletxt.setText("Verification");
                    binding.desctxt.setText("We texted you a code to verify your phone number.");
                    signup();
                    step++;
                }
            }

        });
        binding.linkLogin.setOnClickListener(v -> {
            // Finish the registration screen and return to the Login activity
            finish();
        });
    }

    private void verifyCode() {

        String code = Objects.requireNonNull(binding.verificationcodeet.getText()).toString();
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        if (code.isEmpty()) {
            Toast.makeText(this, "Code Required", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            try {
                Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } catch (Exception e) {
                Toast toast = Toast.makeText(this, "Verification Code is wrong " + e, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private void hideall() {
        binding.namell.setVisibility(View.GONE);
        binding.emailll.setVisibility(View.GONE);
        binding.passwordll.setVisibility(View.GONE);
        binding.phonenumberll.setVisibility(View.GONE);
        binding.phonenumbertextll.setVisibility(View.GONE);
        binding.verificationTab.setVisibility(View.GONE);
    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
        } else {
            binding.userinfoTab.setVisibility(View.GONE);
            binding.verificationTab.setVisibility(View.VISIBLE);
        }
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Verification Code...");
        progressDialog.show();
        new Handler().postDelayed(() -> {
            progressDialog.dismiss();
            signUpViewModel.getUser().observe(this, signUpData -> {

            });
            finish();
        }, 5000);

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;
        String name = binding.inputName.getText().toString();
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        String mobile = binding.inputPhoneNumber.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            binding.inputName.setError("at least 3 characters");
            valid = false;
        } else {
            binding.inputName.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("enter a valid email address");
            binding.inputEmail.setFocusable(true);
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            binding.inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }
        if (mobile.isEmpty() || mobile.length() <= 10 || password.length() >= 10) {
            binding.inputPhoneNumber.setError("number should be 10 numbers ");
            valid = false;
        } else {
            binding.inputPhoneNumber.setError(null);
        }
        return valid;
    }

    private boolean checkValidName(String UserFullName) {
        try {
            int firstSpace = UserFullName.indexOf(" "); // detect the first space character
            firstName = UserFullName.substring(0, firstSpace);  // get everything upto the first space character
            lastName = UserFullName.substring(firstSpace).trim(); // get everything after the first space, trimming the spaces off
            Log.e(TAG, "i am here i got first name and last name and first name is " + firstName);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "i am here i did not got first name and last name");
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivityStudent
        moveTaskToBack(true);
    }
}
