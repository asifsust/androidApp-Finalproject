package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.RemoveEmployeeResponse;
import com.example.employees.my_interface.SimpleCallBack;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoveEmployeeApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final UserSession userSession;
    private final String id;
    private final CustomLoadingDialog loadingDialog;
    private final SimpleCallBack callBack;

    public RemoveEmployeeApi(Context context, String id, SimpleCallBack callBack) {
        this.context = context;
        this.id = id;
        this.callBack = callBack;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void removeEmployee(){
        loadingDialog.start();
        String url = "/api/user/"+id;
        Call<RemoveEmployeeResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .RemoveEmployee(url,"Bearer "+userSession.getToken());

        call.enqueue(new Callback<RemoveEmployeeResponse>() {
            @Override
            public void onResponse(@NonNull Call<RemoveEmployeeResponse> call, @NonNull Response<RemoveEmployeeResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    callBack.callback();
                    Toast.makeText(context, "Employee Removed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: employee removed");
                    Log.d(TAG, "onResponse: msg: "+ Objects.requireNonNull(response.body()).getMessage());
                }else {
                    Log.d(TAG, "onResponse: not successful");
                    callBack.callback();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RemoveEmployeeResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: response failure: "+t.getMessage());
                loadingDialog.dismiss();
                callBack.callback();
            }
        });
    }
}
