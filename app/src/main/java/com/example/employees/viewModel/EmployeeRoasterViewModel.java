package com.example.employees.viewModel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;

import com.example.employees.network.callingApi.LogoutApi;

public class EmployeeRoasterViewModel extends ViewModel {

    private final String TAG = "emp_ros_";
    private Context context;

    public EmployeeRoasterViewModel(Context context) {
        this.context = context;
    }

    public void logoutClick(){
        LogoutApi logoutApi = new LogoutApi(context);
        logoutApi.LogoutUser();
//        Log.d(TAG, "OnLogoutClick: activated");
//        Toast.makeText(context, "Log out", Toast.LENGTH_SHORT).show();
    }

    public void settingClick(){
        Toast.makeText(context, "Setting", Toast.LENGTH_SHORT).show();
    }

    public void drawerCloseClick(){
        //.closeDrawer(GravityCompat.START);
    }
}
