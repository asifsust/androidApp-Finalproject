package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.addWard.AddWardResponse;
import com.example.employees.my_interface.SimpleCallBack;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateWardApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final String wardId;
    private final String wardName;
    private final CustomLoadingDialog loadingDialog;
    private final UserSession userSession;
    private final SimpleCallBack simpleCallBack;

    public UpdateWardApi(Context context, String wardId, String wardName, SimpleCallBack simpleCallBack) {
        this.context = context;
        this.wardId = wardId;
        this.wardName = wardName;
        this.simpleCallBack = simpleCallBack;
        loadingDialog = new CustomLoadingDialog(context);
        userSession = new UserSession(context);
    }

    public void updateWard(){
        loadingDialog.start();
        String url = "/api/wards/"+ wardId;

        Call<AddWardResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .UpdateWard("Bearer "+userSession.getToken(),url, wardName);

        call.enqueue(new Callback<AddWardResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddWardResponse> call, @NonNull Response<AddWardResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: ward update response found");
                    Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    simpleCallBack.callback();
                }else Toast.makeText(context, "Failed to update ward, try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<AddWardResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: update ward response failure: "+t.getMessage());
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
