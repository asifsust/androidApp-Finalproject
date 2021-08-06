package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.ChangePassResponse;
import com.example.employees.model.RemoveEmployeeResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeImageApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final MultipartBody.Part image;
    private final UserSession userSession;
    private final CustomLoadingDialog loadingDialog;

    public ChangeImageApi(Context context, MultipartBody.Part image) {
        this.context = context;
        this.image = image;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void changeImage(){
        loadingDialog.start();
        Call<ChangePassResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .ChangeImage("Bearer "+userSession.getToken(),image);

        call.enqueue(new Callback<ChangePassResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChangePassResponse> call, @NonNull Response<ChangePassResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: change image response success");
                }
                else {
                    Log.d(TAG, "onResponse: response issue found");
                    Toast.makeText(context, "response problem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangePassResponse> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Log.d(TAG, "onFailure: change image response failure: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
