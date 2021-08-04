package com.example.employees.model;

import android.text.TextUtils;
import android.util.Patterns;

public class User {
    private String email;
    private String password;

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
        return this.email != null && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(){
        return this.password != null && password.length()>=6;
    }

    public String printWelcomeMessage(){
        return "Welcome "+this.email;
    }
}
