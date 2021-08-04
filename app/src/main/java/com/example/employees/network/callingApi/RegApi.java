package com.example.employees.network.callingApi;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.example.employees.R;
import com.example.employees.app.AppController;
import com.example.employees.model.RegResponse;
import com.example.employees.network.RetrofitClient;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class RegApi {
    private static final String TAG = "reg_api";
    private final RequestBody name;
    private final RequestBody mobile;
    private final RequestBody email;
    private final RequestBody password;
    private final RequestBody password_confirmation;
    private final MultipartBody.Part image;
    private final RequestBody date_of_birth;
    private final RequestBody joining_date;
    private final RequestBody user_id;
    private final RequestBody role_id;
    private final Context context;
    private final NavController navController;

    public RegApi(RequestBody name, RequestBody mobile, RequestBody email, RequestBody password, RequestBody password_confirmation, MultipartBody.Part image,RequestBody date_of_birth, RequestBody joining_date, RequestBody user_id, RequestBody role_id, Context context,NavController navController) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.image = image;
        this.date_of_birth = date_of_birth;
        this.joining_date = joining_date;
        this.user_id = user_id;
        this.role_id = role_id;
        this.context = context;
        this.navController = navController;

    }

    public void registerEmployee(){

        Log.d(TAG, "registerEmployee: name: "+bodyToString(name));
        Log.d(TAG, "registerEmployee: mobile: "+bodyToString(mobile));

        if (image == null)
            Log.d(TAG, "registerEmployee: image: null");

        Call<RegResponse> call;
        if (image == null && bodyToString(user_id).equals("")){
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .RegisterBoth(bodyToString(name),bodyToString(mobile),bodyToString(email),bodyToString(password),bodyToString(password_confirmation),bodyToString(date_of_birth),bodyToString(joining_date),bodyToString(role_id));
        }
        else if (bodyToString(user_id).equals("")){
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .RegisterWithoutUserId(name,mobile,email,password,password_confirmation,date_of_birth,joining_date,image,role_id);
        }
        else if (image == null){
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .RegisterWithoutImage(bodyToString(name),bodyToString(mobile),bodyToString(email),bodyToString(password),bodyToString(password_confirmation),bodyToString(date_of_birth),bodyToString(joining_date),bodyToString(user_id),bodyToString(role_id));
        }
        else {
            call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .Register(name,mobile,email,password,password_confirmation,date_of_birth,joining_date,user_id,role_id,image);
        }

        call.enqueue(new Callback<RegResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegResponse> call, @NonNull Response<RegResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Employee added successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Successfully register");
                    Log.d(TAG, "onResponse: image: "+Objects.requireNonNull(response.body()).getEmployee().getImage());
                    Log.d(TAG, "onResponse: name: "+ Objects.requireNonNull(response.body()).getUser().getName());
                    Log.d(TAG, "onResponse: emp_user_id: "+ Objects.requireNonNull(response.body()).getEmployee().getUserId());
                    navController.navigate(R.id.action_addNewEmployeeFragment_to_employeesFragment);

                }else {
                    Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: response_failure: "+t.getMessage());
            }
        });
    }

    private static String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }
}
