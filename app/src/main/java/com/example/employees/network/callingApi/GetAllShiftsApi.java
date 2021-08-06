package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.adapter.AllAssignShiftsAdapter;
import com.example.employees.model.allAssignShifts.AssignShiftInfo;
import com.example.employees.model.allAssignShifts.Datum;
import com.example.employees.model.allAssignShifts.GetAssignShiftsResponse;
import com.example.employees.my_interface.TodayBooking;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllShiftsApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final UserSession userSession;
    private RecyclerView recyclerView;
    private CustomLoadingDialog loadingDialog;
    private AllAssignShiftsAdapter adapter;
    private NavController navController;
    private final String userId;
    private String date;
    private TodayBooking todayBooking;

    public GetAllShiftsApi(Context context, RecyclerView recyclerView,NavController navController,String userId) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.navController = navController;
        this.userId = userId;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public GetAllShiftsApi(Context context,String date,String userId,TodayBooking todayBooking){
        this.context = context;
        this.date = date;
        this.userId = userId;
        this.todayBooking = todayBooking;
        userSession = new UserSession(context);
    }

    public void allShiftByDate(){
        Call<GetAssignShiftsResponse> call;
        call = RetrofitClient
                .getInstance()
                .getApi()
                .AllAssignShiftByDate("Bearer "+userSession.getToken(),userId,date);


        call.enqueue(new Callback<GetAssignShiftsResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetAssignShiftsResponse> call, @NonNull Response<GetAssignShiftsResponse> response) {
                if (response.isSuccessful()){
                    String message = "ok";
                    Log.d(TAG, "onResponse: all assign shifts by date success");

                    GetAssignShiftsResponse mainModel = response.body();
                    ArrayList<Datum> dataList = new ArrayList<>(Objects.requireNonNull(mainModel).getData());
                    ArrayList<AssignShiftInfo> assignShiftInfoList;

                    if (dataList.size() != 0){
                        assignShiftInfoList = new ArrayList<>(dataList.get(0).getAssignShiftInfo());
                        if (assignShiftInfoList.size() != 0){
                            for (int i=0;i<assignShiftInfoList.size();i++){
                                AssignShiftInfo info = assignShiftInfoList.get(i);
                                if (String.valueOf(info.getUser().getId()).equals(userId)){
                                    message = "Already booked, Try with another date";
                                    break;
                                }
                            }
                        }
                    }

                    try {
                        todayBooking.isBookedToday(message);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        todayBooking.isBookedToday("Something went wrong, please try again");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetAssignShiftsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: all assign shifts by date response failure: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                try {
                    todayBooking.isBookedToday(t.getMessage());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void allShift(){
        loadingDialog.start();

        Call<GetAssignShiftsResponse> call;
        if (userId.equals("")){
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .AllAssignShift("Bearer "+userSession.getToken());
        }else {
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .AllAssignShiftEmp("Bearer "+userSession.getToken(),userId);
        }

        
        call.enqueue(new Callback<GetAssignShiftsResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetAssignShiftsResponse> call, @NonNull Response<GetAssignShiftsResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: all assign shifts success");
                    GetAssignShiftsResponse mainModel = response.body();

                    adapter = new AllAssignShiftsAdapter(context, Objects.requireNonNull(mainModel).getData(),navController);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetAssignShiftsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: all assign shifts response failure: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }


}
