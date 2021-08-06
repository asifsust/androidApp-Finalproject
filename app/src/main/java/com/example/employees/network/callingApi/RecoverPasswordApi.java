package com.example.employees.network.callingApi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.LoginActivity;
import com.example.employees.R;
import com.example.employees.model.RecoverPasswordResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoverPasswordApi {
    private final Context context;
    private final String email;
    private final String password;
    private final CustomLoadingDialog loadingDialog;

    public RecoverPasswordApi(Context context, String email, String password) {
        this.context = context;
        this.email = email;
        this.password = password;
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void recoverPassword(){
        loadingDialog.start();
        Call<RecoverPasswordResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .RecoverPassword(email,password,password);

        call.enqueue(new Callback<RecoverPasswordResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecoverPasswordResponse> call, @NonNull Response<RecoverPasswordResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    if (Objects.requireNonNull(response.body()).getMessage().equals(context.getString(R.string.successfully_updated))){
                        Toast.makeText(context, context.getString(R.string.successfully_updated), Toast.LENGTH_SHORT).show();
                        gotoLoginActivity();
                    }else Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else Toast.makeText(context, "Failed to reset password,try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<RecoverPasswordResponse> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
