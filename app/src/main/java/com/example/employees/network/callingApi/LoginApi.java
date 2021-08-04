package com.example.employees.network.callingApi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.EmployeeHomeActivity;
import com.example.employees.ManagerHomeActivity;
import com.example.employees.R;
import com.example.employees.model.LoginResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginApi {
    private static final String TAG = "login_api";
    private final String email;
    private final String password;
    private final Context context;
    private final UserSession userSession;

    public LoginApi(String email, String password, Context context) {
        this.email = email;
        this.password = password;
        this.context = context;
        userSession = new UserSession(context);
    }

    public void loginUser(){
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .Login(email,password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, R.string.login_successful, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: success");

                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "onResponse: token: "+ Objects.requireNonNull(loginResponse).getToken());
                    Intent intent;
                    if (Objects.requireNonNull(loginResponse).getUser().getRoleId() != 1){
                        intent = new Intent(context, EmployeeHomeActivity.class);
                    }else {
                        userSession.setAdminData(email,password);
                        intent = new Intent(context, ManagerHomeActivity.class);
                    }

                    String role;
                    if (loginResponse.getUser().getRoleId() == 1 ) role = "Manager";
                    else {
                        if (loginResponse.getUser().getRoleId() == 2) role = "RGN";
                        else role = "HCS";
                    }
                    setUserSession(loginResponse.getUser().getRoleId(),loginResponse.getToken(),role);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                    ((Activity)context).finish();



                }else {
                    Toast.makeText(context, R.string.email_or_pass_is_incorrect, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: response failure: "+t.getMessage());
            }
        });
    }

    private void setUserSession(Integer roleId, String token,String role) {
        UserSession userSession = new UserSession(context);
        userSession.setLoginStatus(true);
        userSession.setRoleId(roleId);
        userSession.setToken(token);
        userSession.setRole(role);
        Log.d(TAG, "setUserSession: token:  "+token);
    }
}
