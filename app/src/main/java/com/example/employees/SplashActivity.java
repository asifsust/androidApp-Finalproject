package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.employees.session.UserSession;

public class SplashActivity extends AppCompatActivity {

    public UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        userSession = new UserSession(this);

        new Handler().postDelayed(() -> {
            //login();
            if (!userSession.getLoginStatus()) login();
            else {
                if (userSession.getRoleId() == 1) managerHome();
                else employeeHome();
            }
        }, 5000);
    }

    private void managerHome() {
        startActivity(new Intent(this,ManagerHomeActivity.class));
        finish();
    }

    private void employeeHome() {
        startActivity(new Intent(this,EmployeeHomeActivity.class));
        finish();
    }

    void login(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}