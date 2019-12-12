package com.volksoftech.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.volksoftech.sample.dump.RecyclerViewWithImageCapture;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    OtpView verificationcodeet;
    EditText input_phonenumber, _passwordText, _nameText, _emailText;
    Button _signupButton;
    ImageView backbtn;
    String emailst, passst, namest, phonenumber;
    TextView phonenumbertxtll, titletxt, desctxt;
    LinearLayout userinfo_tab, verification_tab, _loginLink, namell, emailll, passwordll, phonenumberll;
    int step = 1;
    //name strings
    String lastName = "";
    String firstName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Initialize Firebase Auth
        _passwordText = findViewById(R.id.input_password);
        _nameText = findViewById(R.id.input_name);
        _emailText = findViewById(R.id.input_email);
        _signupButton = findViewById(R.id.btn_signup);
        input_phonenumber = findViewById(R.id.input_phoneNumber);
        _loginLink = findViewById(R.id.link_login);
        namell = findViewById(R.id.namell);
        emailll = findViewById(R.id.emailll);
        passwordll = findViewById(R.id.passwordll);
        phonenumberll = findViewById(R.id.phonenumberll);
        phonenumbertxtll = findViewById(R.id.phonenumbertextll);
        titletxt = findViewById(R.id.titletxt);
        desctxt = findViewById(R.id.desctxt);
        userinfo_tab = findViewById(R.id.userinfo_tab);
        verification_tab = findViewById(R.id.verification_tab);
        verification_tab.setVisibility(View.GONE);
        verificationcodeet = findViewById(R.id.verificationcodeet);
        verificationcodeet.setOtpCompletionListener(otp -> {

        });
        Button btn = findViewById(R.id.verificationcodebtn);
        backbtn = findViewById(R.id.backbtn);
        hideall();
        namell.setVisibility(View.VISIBLE);
        /*Setting on click listeners*/
        backbtn.setOnClickListener(v -> onBackPressed());
        btn.setOnClickListener(v -> verifyCode());
        _signupButton.setOnClickListener(v -> {
            if (step == 1) {
                namest = _nameText.getText().toString();
                if (namest.isEmpty() || havelastname(namest)) {
                    if (namest.isEmpty()) {
                        Toast.makeText(SignUpActivity.this, "Please write your name!", Toast.LENGTH_SHORT).show();
                    } else if (havelastname(namest)) {
                        Toast.makeText(SignUpActivity.this, "Please add full name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    namell.setVisibility(View.GONE);
                    emailll.setVisibility(View.VISIBLE);
                    titletxt.setText("Choose email");
                    desctxt.setText("enter your email address below.");
                    step++;
                }
            } else if (step == 2) {
                emailst = _emailText.getText().toString();
                if (emailst.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please write your email!", Toast.LENGTH_SHORT).show();
                } else {
                    emailll.setVisibility(View.GONE);
                    passwordll.setVisibility(View.VISIBLE);
                    titletxt.setText("Create Password");
                    desctxt.setText("Your password must have atleast one symbol & 4 or more characters.");
                    step++;
                }

            } else if (step == 3) {
                passst = _passwordText.getText().toString();
                if (!passst.isEmpty()) {
                    passwordll.setVisibility(View.GONE);
                    phonenumberll.setVisibility(View.VISIBLE);
                    phonenumbertxtll.setVisibility(View.VISIBLE);
                    titletxt.setText("Let's Get Started");
                    desctxt.setText("Enter your mobile number to enable 2-step verification.");
                    step++;
                }
            } else if (step == 4) {
                phonenumber = input_phonenumber.getText().toString();
                if (!phonenumber.isEmpty()) {
                    phonenumberll.setVisibility(View.GONE);
                    phonenumbertxtll.setVisibility(View.GONE);
                    titletxt.setText("Verification");
                    desctxt.setText("We texted you a code to verify your phone number.");
                    signup();
                    step++;
                }
            }

        });
        _loginLink.setOnClickListener(v -> {
            // Finish the registration screen and return to the Login activity
            finish();
        });
    }

    private void verifyCode() {

        String code = Objects.requireNonNull(verificationcodeet.getText()).toString();
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
        namell.setVisibility(View.GONE);
        emailll.setVisibility(View.GONE);
        passwordll.setVisibility(View.GONE);
        phonenumberll.setVisibility(View.GONE);
        phonenumbertxtll.setVisibility(View.GONE);
    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        } else {
            userinfo_tab.setVisibility(View.GONE);
            verification_tab.setVisibility(View.VISIBLE);
        }
        _signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Verification Code...");
        progressDialog.show();
        new Handler().postDelayed(() -> {
            progressDialog.dismiss();
            finish();
        }, 5000);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            _emailText.setFocusable(true);
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }

    private boolean havelastname(String UserFullName) {
        Log.e(TAG, "i am here iin have last name function");
        try {
            int firstSpace = UserFullName.indexOf(" "); // detect the first space character
            firstName = UserFullName.substring(0, firstSpace);  // get everything upto the first space character
            lastName = UserFullName.substring(firstSpace).trim(); // get everything after the first space, trimming the spaces off
            Log.e(TAG, "i am here i got first name and last name and firstname is " + firstName);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "i am here i did not got first name and last name");
            return true;
        }
    }
}
