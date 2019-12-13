package com.volksoftech.sample.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Patterns;

public class User implements Parcelable {
    private String FullName;
    private String LoginId;
    private String Password;
    private String MobileNumber;

    public User(Parcel in) {
        LoginId = in.readString();
        Password = in.readString();
    }

    public User(String email, String pass) {
        LoginId = email;
        Password = pass;
    }

    public User(String fullName, String loginId, String password, String mobileNumber) {
        FullName = fullName;
        LoginId = loginId;
        Password = password;
        MobileNumber = mobileNumber;
    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    //validations


    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getLoginId()).matches();
    }

    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(LoginId);
        parcel.writeString(Password);
        parcel.writeString(FullName);
        parcel.writeString(MobileNumber);
    }
}
