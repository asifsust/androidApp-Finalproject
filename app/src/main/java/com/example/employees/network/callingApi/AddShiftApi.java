package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.addShift.AddShiftResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShiftApi {
    private final String TAG = this.getClass().getName();
    public final Context context;
    public final String shiftName;
    public final String startTime;
    public final String endTime;
    public final UserSession userSession;
    public final CustomLoadingDialog loadingDialog;

    public AddShiftApi(Context context, String shiftName, String startTime, String endTime) {
        this.context = context;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void addShift(){
        loadingDialog.start();
        Call<AddShiftResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .AddShift("Bearer "+userSession.getToken(),shiftName,startTime,endTime);

        call.enqueue(new Callback<AddShiftResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddShiftResponse> call, @NonNull Response<AddShiftResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: add shift response found");
                    String msg = Objects.requireNonNull(response.body()).getMessage();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, "Failed to add shift, try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<AddShiftResponse> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Log.d(TAG, "onFailure: add shift response failure: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
