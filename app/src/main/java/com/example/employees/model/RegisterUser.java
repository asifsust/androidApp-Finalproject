package com.example.employees.model;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

public class RegisterUser {
    private String email;
    private String password;
    private String confirm_password;

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValidEmail(){
        Log.d("me_s", "isValidEmail: email: "+email);
        return this.email != null && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(){
        return this.password != null && password.length()>=6;
    }

    public boolean isPasswordMatched(){
        return ((this.password != null && this.confirm_password != null) && this.password.equals(this.confirm_password));
    }

}
