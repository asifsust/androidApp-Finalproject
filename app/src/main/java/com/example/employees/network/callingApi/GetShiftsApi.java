package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.employees.model.GetShiftsResponse;
import com.example.employees.my_interface.CallBack;
import com.example.employees.network.RetrofitClient;
import com.example.employees.session.UserSession;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetShiftsApi {
    private final String TAG = this.getClass().getName();
    private final UserSession userSession;
    private final CallBack callBack;

    public GetShiftsApi(Context context, CallBack callBack) {
        this.callBack = callBack;
        userSession = new UserSession(context);

    }

    public void getShift(){
        Call<ArrayList<GetShiftsResponse>> call = RetrofitClient
                .getInstance()
                .getApi()
                .AllShifts("Bearer "+userSession.getToken());

        call.enqueue(new Callback<ArrayList<GetShiftsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<GetShiftsResponse>> call, @NonNull Response<ArrayList<GetShiftsResponse>> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: shifts retrieve success");
                    callBack.getShiftCallBack(response.body());
                }else {
                    Log.d(TAG, "onResponse: shifts not success");
                    ArrayList<GetShiftsResponse> list = new ArrayList<>();
                    callBack.getShiftCallBack(list);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<GetShiftsResponse>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: shifts retrieve response failure: "+t.getMessage());
                ArrayList<GetShiftsResponse> list = new ArrayList<>();
                callBack.getShiftCallBack(list);
            }
        });
    }


}
