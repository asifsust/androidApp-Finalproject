package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.CancelShiftResponse;
import com.example.employees.my_interface.SimpleCallBack;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteWardApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final String wardId;
    private final CustomLoadingDialog loadingDialog;
    private final UserSession userSession;
    private final SimpleCallBack simpleCallBack;

    public DeleteWardApi(Context context, String shiftId,SimpleCallBack simpleCallBack) {
        this.context = context;
        this.wardId = shiftId;
        this.simpleCallBack = simpleCallBack;
        loadingDialog = new CustomLoadingDialog(context);
        userSession = new UserSession(context);
    }

    public void deleteWard(){
        loadingDialog.start();
        String url = "/api/wards/"+ wardId;

        Call<CancelShiftResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .DeleteWard("Bearer "+userSession.getToken(),url);

        call.enqueue(new Callback<CancelShiftResponse>() {
            @Override
            public void onResponse(@NonNull Call<CancelShiftResponse> call, @NonNull Response<CancelShiftResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: ward delete response found");
                    Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    simpleCallBack.callback();
                }else Toast.makeText(context, "Failed to delete ward, try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<CancelShiftResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: delete ward response failure: "+t.getMessage());
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
