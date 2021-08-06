package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.updateShift.UpdateShiftResponse;
import com.example.employees.my_interface.SimpleCallBack;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateShiftApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final String shiftId;
    private final String shiftName;
    private final String startTime;
    private final String endTime;
    private final CustomLoadingDialog loadingDialog;
    private final UserSession userSession;
    private final SimpleCallBack simpleCallBack;

    public UpdateShiftApi(Context context, String shiftId, String shiftName, String startTime, String endTime, SimpleCallBack simpleCallBack) {
        this.context = context;
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.simpleCallBack = simpleCallBack;
        loadingDialog = new CustomLoadingDialog(context);
        userSession = new UserSession(context);
    }

    public void updateShift(){
        loadingDialog.start();
        String url = "/api/shifts/"+shiftId;

        Call<UpdateShiftResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .UpdateShift("Bearer "+userSession.getToken(),url,shiftName,startTime,endTime);

        call.enqueue(new Callback<UpdateShiftResponse>() {
            @Override
            public void onResponse(@NonNull Call<UpdateShiftResponse> call, @NonNull Response<UpdateShiftResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: shift update response found");
                    Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    simpleCallBack.callback();
                }else Toast.makeText(context, "Failed to update shift, try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<UpdateShiftResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: update shift response failure: "+t.getMessage());
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
