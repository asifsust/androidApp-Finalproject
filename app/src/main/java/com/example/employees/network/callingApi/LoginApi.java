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
import com.example.employees.ResetPasswordActivity;
import com.example.employees.model.CancelShiftResponse;
import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.login.LoginResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.others.GlobalValues;
import com.example.employees.session.UserSession;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginApi {
    private final String TAG = this.getClass().getName();
    private final String email;
    private final String password;
    private final Context context;
    private final CustomLoadingDialog loadingDialog;

    public LoginApi(String email, String password, Context context) {
        this.email = email;
        this.password = password;
        this.context = context;
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void loginUser(){
        loadingDialog.start();
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .Login(email,password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: success");
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "onResponse: token: "+ Objects.requireNonNull(loginResponse).getToken());

                    if (loginResponse.getEmployee() == null
                            || (loginResponse.getEmployee() != null
                            && loginResponse.getEmployee().getStatus() != 1)){
                        Toast.makeText(context, "User not registered!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(context, R.string.login_successful, Toast.LENGTH_SHORT).show();

                    UserSession userSession = new UserSession(context);
                    userSession.setEmployeeDataUser(loginResponse.getUser());
                    userSession.setEmployeeDataEmp(loginResponse.getEmployee());

                    Intent intent;
                    int roleId;
                    if (loginResponse.getUser().getRole().equals("manager")){
                        roleId = 1;
                        setUserSession(roleId,loginResponse.getToken(),loginResponse.getUser().getRole(),true);
                        userSession.setAdminData(email,password);
                        intent = new Intent(context, ManagerHomeActivity.class);

                    }else {
                        if (loginResponse.getUser().getRole().equals("RGN")) roleId = 2;
                        else roleId = 3;

                        if (loginResponse.getUser().getIsUpdatePassword() == 1){
                            setUserSession(roleId,loginResponse.getToken(),loginResponse.getUser().getRole(),true);
                            intent = new Intent(context, EmployeeHomeActivity.class);
                        }else {
                            GlobalValues.isManager = false;
                            intent = new Intent(context, ResetPasswordActivity.class);
                            intent.putExtra("current_pass",password);
                            setUserSession(roleId,loginResponse.getToken(),loginResponse.getUser().getRole(),false);
                        }
                    }

                    context.startActivity(intent);
                    ((Activity)context).finish();

                }
                else {
                    loadingDialog.dismiss();
                    Toast.makeText(context, R.string.email_or_pass_is_incorrect, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: response failure: "+t.getMessage());
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserSession(Integer roleId, String token,String role,boolean loginStatus) {
        UserSession userSession = new UserSession(context);
        userSession.setLoginStatus(loginStatus);
        userSession.setRoleId(roleId);
        userSession.setToken(token);
        userSession.setRole(role);
        Log.d(TAG, "setUserSession: token:  "+token);
    }
}
