package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.R;
import com.example.employees.model.VerifyEmailResponse;
import com.example.employees.my_interface.ReturnCode;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final String email;
    private final CustomLoadingDialog loadingDialog;
    private final ReturnCode callBack;

    public VerifyEmailApi(Context context, String email,ReturnCode callBack) {
        this.context = context;
        this.email = email;
        this.callBack = callBack;
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void verifyEmail(){
        loadingDialog.start();
        Call<VerifyEmailResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .VerifyEmail(email);

        call.enqueue(new Callback<VerifyEmailResponse>() {
            @Override
            public void onResponse(@NonNull Call<VerifyEmailResponse> call, @NonNull Response<VerifyEmailResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: verify email response success");
                    if (Objects.requireNonNull(response.body()).getMessage().contains(context.getString(R.string.successfully_send))){
                        callBack.MyCode(response.body().getCode());
                    }else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(context, "Failed to verify email,try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<VerifyEmailResponse> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
