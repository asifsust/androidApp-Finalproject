package com.example.employees.network.callingApi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.LoginActivity;
import com.example.employees.model.LogoutResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutApi {

    private final String TAG = this.getClass().getName();
    public Context context;
    public UserSession userSession;
    //private CustomLoadingDialog loadingDialog;

    public LogoutApi(Context context) {
        this.context = context;
        userSession = new UserSession(context);
        //loadingDialog = new CustomLoadingDialog(context);
    }

    public void LogoutUser(){
        Log.d(TAG, "LogoutUser: token: "+userSession.getToken());
        goToLogin();
        userSession.clearUserSession();

        //loadingDialog.start();
//        Call<LogoutResponse> call = RetrofitClient
//                .getInstance()
//                .getApi()
//                .Logout("Bearer "+userSession.getToken());
//
//        call.enqueue(new Callback<LogoutResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<LogoutResponse> call, @NonNull Response<LogoutResponse> response) {
//                //loadingDialog.dismiss();
//                if (response.isSuccessful()){
//                    Toast.makeText(context, "Successfully logged out", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "onResponse: logout success: ");
//                    userSession.clearUserSession();
//                    goToLogin();
//
//                }else {
//                    Log.d(TAG, "onResponse: logout failure occurred: "+response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<LogoutResponse> call, @NonNull Throwable t) {
//                Log.d(TAG, "onFailure: logout response failed: "+t.getMessage());
//                //loadingDialog.dismiss();
//                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void goToLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
