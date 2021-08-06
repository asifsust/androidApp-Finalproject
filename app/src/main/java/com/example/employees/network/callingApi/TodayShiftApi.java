package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.todayAssignShifts.TodayAssignShiftsResponse;
import com.example.employees.my_interface.TodayShiftCallBack;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayShiftApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final UserSession userSession;
    private final TodayShiftCallBack callBack;
    private final CustomLoadingDialog loadingDialog;

    public TodayShiftApi(Context context, TodayShiftCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void todayShift(){
        loadingDialog.start();
        Call<TodayAssignShiftsResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .TodayAssignShift("Bearer "+userSession.getToken());

        call.enqueue(new Callback<TodayAssignShiftsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TodayAssignShiftsResponse> call, @NonNull Response<TodayAssignShiftsResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: today shift response successfull");
                    callBack.todayShiftListener(response.body());
                }else {
                    Log.d(TAG, "onResponse: today shift response unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TodayAssignShiftsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: today shift response failure: "+t.getMessage());
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
