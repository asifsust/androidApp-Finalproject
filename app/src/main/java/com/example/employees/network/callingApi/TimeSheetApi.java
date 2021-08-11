package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.adapter.TimeSheetAdapter;
import com.example.employees.model.timeSheet.TimeSheetResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeSheetApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final String userId;
    private final RecyclerView recyclerView;
    private final CustomLoadingDialog loadingDialog;
    private final UserSession userSession;
    private TimeSheetAdapter adapter;

    public TimeSheetApi(Context context, RecyclerView recyclerView, String userId) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.userId = userId;
        loadingDialog = new CustomLoadingDialog(context);
        userSession = new UserSession(context);
    }

    public void timeSheet(){
        loadingDialog.start();
        Call<TimeSheetResponse> call;
        if (userId.equals("")){
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .TimeSheetManager("Bearer "+userSession.getToken());
        }else {
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .TimeSheetEmployee("Bearer "+userSession.getToken(),userId);
        }

        call.enqueue(new Callback<TimeSheetResponse>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheetResponse> call, @NonNull Response<TimeSheetResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: success");
                    if (Objects.requireNonNull(response.body()).getTimesheets().size() != 0){
                        adapter = new TimeSheetAdapter(context, Objects.requireNonNull(response.body()).getTimesheets());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                   }
                          else Toast.makeText(context, "No Timesheet found", Toast.LENGTH_SHORT).show();

                }else {
                    Log.d(TAG, "onResponse: something went wrong");
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheetResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: timesheet response failure: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }
}
