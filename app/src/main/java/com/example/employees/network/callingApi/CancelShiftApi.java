package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.example.employees.model.CancelShiftResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.others.GlobalMethods;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelShiftApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final UserSession userSession;
    private final CustomLoadingDialog loadingDialog;
    private final String shiftId;
    private final NavController navController;

    public CancelShiftApi(Context context, String shiftId, NavController navController) {
        this.context = context;
        this.shiftId = shiftId;
        this.navController = navController;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void cancelShift(){

        loadingDialog.start();
        String url = "/api/delete-user-shift/"+shiftId;

        Call<CancelShiftResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .CancelShift(url,"Bearer "+userSession.getToken());

        call.enqueue(new Callback<CancelShiftResponse>() {
            @Override
            public void onResponse(@NonNull Call<CancelShiftResponse> call, @NonNull Response<CancelShiftResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: cancel shift response found");
                    Log.d(TAG, "onResponse: message: "+ Objects.requireNonNull(response.body()).getMessage());
                    assert response.body() != null;
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    GlobalMethods.refreshCurrentFragment(navController);

                }else {
                    Log.d(TAG, "onResponse: cancel shift unsuccessful");
                    Toast.makeText(context, "Failed to cancel shift, Try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CancelShiftResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: cancel shift response failure: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }
}
