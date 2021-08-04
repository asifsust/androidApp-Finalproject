package com.example.employees.app;

import android.app.Application;

public class AppController extends Application {
    public static AppController instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
