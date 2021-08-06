package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.employees.adapter.EmployeesAdapter;
import com.example.employees.model.employees.EmployeeData;
import com.example.employees.model.employees.EmployeesResponse;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeesApi {
    private final Context context;
    private final String TAG = this.getClass().getName();
    private final EmployeesAdapter adapter;
    private final ArrayList<EmployeeData> employeeList;
    private final UserSession userSession;
    private final CustomLoadingDialog loadingDialog;

    public EmployeesApi(Context context, ArrayList<EmployeeData> employeeList, EmployeesAdapter adapter) {
        this.employeeList = employeeList;
        this.adapter = adapter;
        this.context = context;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void getEmployees(){
        loadingDialog.start();
        Call<EmployeesResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .Employees("Bearer "+userSession.getToken());

        call.enqueue(new Callback<EmployeesResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmployeesResponse> call, @NonNull Response<EmployeesResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    EmployeesResponse employeesResponse = response.body();
                    employeeList.addAll(Objects.requireNonNull(employeesResponse).getData());

                    for (int i=0;i< employeeList.size();i++){
                        EmployeeData data = employeeList.get(i);
                        if (data.getRole().equals("manager")) {
                            employeeList.remove(i);
                            break;
                        }
                    }

                    for (int i=0;i< employeeList.size();i++){
                        EmployeeData data = employeeList.get(i);
                        data.setSerialId(i+1);
                        employeeList.set(i,data);
                    }

                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: success");
                }else {
                    Log.d(TAG, "onResponse: not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmployeesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: response failure: "+t.getMessage());
                loadingDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
