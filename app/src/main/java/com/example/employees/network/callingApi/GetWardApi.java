package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.employees.model.GetWardsResponse;
import com.example.employees.my_interface.CallBack;
import com.example.employees.network.RetrofitClient;
import com.example.employees.session.UserSession;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetWardApi {
    private final String TAG = this.getClass().getName();
    private final UserSession userSession;
    private final CallBack callBack;

    public GetWardApi(Context context, CallBack callBack) {
        this.callBack = callBack;
        userSession = new UserSession(context);

    }

    public void getWard(){
        Call<ArrayList<GetWardsResponse>> call = RetrofitClient
                .getInstance()
                .getApi()
                .AllWards("Bearer "+userSession.getToken());

        call.enqueue(new Callback<ArrayList<GetWardsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<GetWardsResponse>> call, @NonNull Response<ArrayList<GetWardsResponse>> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: wards retrieve success");
                    callBack.getWardCallBack(response.body());
                }else {
                    Log.d(TAG, "onResponse: wards not success");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<GetWardsResponse>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: wards retrieve response failure: "+t.getMessage());
            }
        });
    }
}
