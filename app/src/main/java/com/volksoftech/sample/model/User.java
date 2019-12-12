package com.volksoftech.sample.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Patterns;

public class User implements Parcelable {
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
    private String LoginId, Password = "";

    public User(Parcel in) {
        LoginId = in.readString();
        Password = in.readString();
    }

    public User(String value, String passwordValue) {

    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getLoginId()).matches();
    }

    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LoginId);
        dest.writeString(Password);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
