package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.example.employees.R;
import com.example.employees.model.assignShift.AssignShiftResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignShiftApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final UserSession userSession;
    private final CustomLoadingDialog loadingDialog;
    private final String user_id;
    private final String shift_id;
    private final String ward_id;
    private final String date;
    private final NavController navController;

    public AssignShiftApi(Context context, String user_id, String shift_id, String ward_id, String date,NavController navController) {
        this.context = context;
        this.user_id = user_id;
        this.shift_id = shift_id;
        this.ward_id = ward_id;
        this.date = date;
        this.navController = navController;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);

    }

    public void assignShift(){
        loadingDialog.start();
        Call<AssignShiftResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .AssignShit("Bearer "+userSession.getToken(),user_id,shift_id,ward_id,date);

        call.enqueue(new Callback<AssignShiftResponse>() {
            @Override
            public void onResponse(@NonNull Call<AssignShiftResponse> call, @NonNull Response<AssignShiftResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: assign shift success");
                    Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    if (navController!=null)
                        navController.navigate(R.id.action_addNewShiftFragment_to_employeesFragment);
                }else {
                    Log.d(TAG, "onResponse: assign shift unsuccessful");
                    Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AssignShiftResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: assign shift response error: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }
}
