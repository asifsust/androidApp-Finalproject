package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.addWard.AddWardResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWardApi {
    private final String TAG = this.getClass().getName();
    public final Context context;
    public final String wardName;
    public final UserSession userSession;
    public final CustomLoadingDialog loadingDialog;

    public AddWardApi(Context context, String wardName) {
        this.context = context;
        this.wardName = wardName;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void addWard(){
        loadingDialog.start();
        Call<AddWardResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .AddWard("Bearer "+userSession.getToken(),wardName);

        call.enqueue(new Callback<AddWardResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddWardResponse> call, @NonNull Response<AddWardResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: add ward response found");
                    Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, "Failed to add ward, try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<AddWardResponse> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Log.d(TAG, "onFailure: add ward response error: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
