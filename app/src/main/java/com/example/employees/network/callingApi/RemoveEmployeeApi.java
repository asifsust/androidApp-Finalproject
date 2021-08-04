package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.model.RemoveEmployeeResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoveEmployeeApi {
    private final String TAG = this.getClass().getName();
    private Context context;
    private UserSession userSession;
    private String id;

    public RemoveEmployeeApi(Context context,String id) {
        this.context = context;
        this.id = id;
        userSession = new UserSession(context);
    }

    public void removeEmployee(){
        String url = "/api/user/"+id;
        Call<RemoveEmployeeResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .RemoveEmployee(url,"Bearer "+userSession.getToken());

        call.enqueue(new Callback<RemoveEmployeeResponse>() {
            @Override
            public void onResponse(@NonNull Call<RemoveEmployeeResponse> call, @NonNull Response<RemoveEmployeeResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Employee Removed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: employee removed");
                    Log.d(TAG, "onResponse: msg: "+ Objects.requireNonNull(response.body()).getMessage());
                }else {
                    Log.d(TAG, "onResponse: not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RemoveEmployeeResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: response failure: "+t.getMessage());
            }
        });
    }
}
