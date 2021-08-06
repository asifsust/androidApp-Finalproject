package com.example.employees.network.callingApi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.EmployeeHomeActivity;
import com.example.employees.ManagerHomeActivity;
import com.example.employees.model.ChangePassResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.others.GlobalValues;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final String currentPass;
    private final String newPass;
    private final UserSession userSession;
    private final CustomLoadingDialog loadingDialog;

    public ChangePassApi(Context context, String currentPass, String newPass) {
        this.context = context;
        this.currentPass = currentPass;
        this.newPass = newPass;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void changePass(){
        loadingDialog.start();

        Log.d(TAG, "changePass: current_pass: "+currentPass);
        Log.d(TAG, "changePass: new_pass: "+newPass);
        Log.d(TAG, "changePass: token: "+userSession.getToken());

        Call<ChangePassResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .ChangePass("Bearer "+userSession.getToken(),currentPass,newPass,newPass);

        call.enqueue(new Callback<ChangePassResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChangePassResponse> call, @NonNull Response<ChangePassResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: change pass response success");
                    userSession.setLoginStatus(true);
                    goHomePage();
                }
                else Log.d(TAG, "onResponse: change pass response unsuccessful");
            }

            @Override
            public void onFailure(@NonNull Call<ChangePassResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: change pass response failure: "+t.getMessage());
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goHomePage() {
        Intent intent;
        if (!GlobalValues.isManager)
            intent = new Intent(context, EmployeeHomeActivity.class);
        else intent = new Intent(context, ManagerHomeActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
