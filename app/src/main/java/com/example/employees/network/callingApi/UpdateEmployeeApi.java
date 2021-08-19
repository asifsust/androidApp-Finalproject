package com.example.employees.network.callingApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.example.employees.R;
import com.example.employees.model.employeeUpdate.Employee;
import com.example.employees.model.employeeUpdate.EmployeeUpdateResponse;
import com.example.employees.model.employeeUpdate.User;
import com.example.employees.network.RetrofitClient;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.session.UserSession;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEmployeeApi {
    private final String TAG = this.getClass().getName();
    private final Context context;
    private final UserSession userSession;

    private final RequestBody name;
    private final RequestBody mobile;
    private final RequestBody email;
    private final MultipartBody.Part image;
    private final RequestBody date_of_birth;
    private final RequestBody joining_date;
    private final RequestBody user_id;
    private final RequestBody role_id;
    private final NavController navController;

    private final String id;
    private final String employee_id;

    private final CustomLoadingDialog loadingDialog;

    public UpdateEmployeeApi(Context context, RequestBody name, RequestBody mobile, RequestBody email, MultipartBody.Part image, RequestBody date_of_birth, RequestBody joining_date, RequestBody user_id, RequestBody role_id, NavController navController, String id, String employee_id) {
        this.context = context;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.image = image;
        this.date_of_birth = date_of_birth;
        this.joining_date = joining_date;
        this.user_id = user_id;
        this.role_id = role_id;
        this.navController = navController;
        this.id = id;
        this.employee_id = employee_id;
        userSession = new UserSession(context);
        loadingDialog = new CustomLoadingDialog(context);
    }

    public void updateEmployee(){
        loadingDialog.start();
        String url = "/api/user/"+id+"/employee/"+employee_id;
        Call<EmployeeUpdateResponse> call;
        if (image != null){
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .UpdateEmployee(
                            url,
                            "Bearer "+userSession.getToken(),
                            name,
                            mobile,
                            email,
                            date_of_birth,
                            joining_date,
                            user_id,
                            role_id,
                            image
                    );
        }else {
            Log.d(TAG, "updateEmployee: url: "+url);
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .UpdateEmployeeWithoutImage(
                            url,
                            "Bearer "+userSession.getToken(),
                            bodyToString(name),
                            bodyToString(mobile),
                            bodyToString(email),
                            bodyToString(date_of_birth),
                            bodyToString(joining_date),
                            bodyToString(user_id),
                            bodyToString(role_id)
                    );
        }


        call.enqueue(new Callback<EmployeeUpdateResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeUpdateResponse> call, @NonNull Response<EmployeeUpdateResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful()){
                    Toast.makeText(context, "Successfully Employee Information Updated!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: success");

                    navController.navigate(R.id.action_addNewEmployeeFragment_to_employeesFragmentForUpdateEmployee);
                }else {
                    Log.d(TAG, "onResponse: not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmployeeUpdateResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: response failure: "+t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private static String bodyToString(final RequestBody request){
        try {
            final Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }
}