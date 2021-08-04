package com.example.employees.network;

import android.graphics.Bitmap;

import com.example.employees.model.LoginResponse;
import com.example.employees.model.LogoutResponse;
import com.example.employees.model.RegResponse;
import com.example.employees.model.RemoveEmployeeResponse;
import com.example.employees.model.employeeUpdate.EmployeeUpdateResponse;
import com.example.employees.model.employees.EmployeesResponse;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiResponse {

    @FormUrlEncoded
    @POST("/api/login")
    Call<LoginResponse> Login(
            @Field("email") String mobile,
            @Field("password") String password
    );

    @Multipart
    @POST("/api/register")
    Call<RegResponse> Register(
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("password_confirmation") RequestBody confirm_password,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("joining_date") RequestBody joining_date,
            @Part("user_id") RequestBody user_id,
            @Part("role_id") RequestBody role_id,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("/api/register")
    Call<RegResponse> RegisterWithoutImage(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String confirm_password,
            @Field("date_of_birth") String date_of_birth,
            @Field("joining_date") String joining_date,
            @Field("user_id") String user_id,
            @Field("role_id") String role_id
    );

    @Multipart
    @POST("/api/register")
    Call<RegResponse> RegisterWithoutUserId(
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("password_confirmation") RequestBody confirm_password,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("joining_date") RequestBody joining_date,
            @Part MultipartBody.Part image,
            @Part("role_id") RequestBody role_id
    );

    @FormUrlEncoded
    @POST("/api/register")
    Call<RegResponse> RegisterBoth(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String confirm_password,
            @Field("date_of_birth") String date_of_birth,
            @Field("joining_date") String joining_date,
            @Field("role_id") String role_id
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/api/logout")
    Call<LogoutResponse> Logout(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/api/employees")
    Call<EmployeesResponse> Employees(@Header("Authorization") String token);


    @Multipart
    @POST()
    Call<EmployeeUpdateResponse> UpdateEmployee(
            @Url String url,
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("password_confirmation") RequestBody confirm_password,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("joining_date") RequestBody joining_date,
            @Part("user_id") RequestBody user_id,
            @Part("role_id") RequestBody role_id,
            @Part MultipartBody.Part image
    );


    @FormUrlEncoded
    @POST()
    Call<EmployeeUpdateResponse> UpdateEmployeeWithoutImage(
            @Url String url,
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String confirm_password,
            @Field("date_of_birth") String date_of_birth,
            @Field("joining_date") String joining_date,
            @Field("user_id") String user_id,
            @Field("role_id") String role_id
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST()
    Call<RemoveEmployeeResponse> RemoveEmployee(
            @Url String url,
            @Header("Authorization") String token
    );

}
