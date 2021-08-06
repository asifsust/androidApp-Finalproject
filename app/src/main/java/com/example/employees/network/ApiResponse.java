package com.example.employees.network;

import com.example.employees.model.CancelShiftResponse;
import com.example.employees.model.ChangePassResponse;
import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.GetWardsResponse;
import com.example.employees.model.LogoutResponse;
import com.example.employees.model.RecoverPasswordResponse;
import com.example.employees.model.RegResponse;
import com.example.employees.model.RemoveEmployeeResponse;
import com.example.employees.model.VerifyEmailResponse;
import com.example.employees.model.addShift.AddShiftResponse;
import com.example.employees.model.addWard.AddWardResponse;
import com.example.employees.model.allAssignShifts.GetAssignShiftsResponse;
import com.example.employees.model.assignShift.AssignShiftResponse;
import com.example.employees.model.employeeUpdate.EmployeeUpdateResponse;
import com.example.employees.model.employees.EmployeesResponse;
import com.example.employees.model.login.LoginResponse;
import com.example.employees.model.timeSheet.TimeSheetResponse;
import com.example.employees.model.todayAssignShifts.TodayAssignShiftsResponse;
import com.example.employees.model.updateShift.UpdateShiftResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
            @Field("email") String email,
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

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/shifts")
    Call<ArrayList<GetShiftsResponse>> AllShifts(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @GET("/api/shifts")
    Call<ArrayList<GetShiftsResponse>> AllShiftsByDate(
            @Header("Authorization") String token,
            @Field("date") String date
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/wards")
    Call<ArrayList<GetWardsResponse>> AllWards(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("/api/user-shift-create")
    Call<AssignShiftResponse> AssignShit(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("shift_id") String shift_id,
            @Field("ward_id") String ward_id,
            @Field("date") String date
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/api/get-all-assign-shift")
    Call<GetAssignShiftsResponse> AllAssignShift(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("/api/get-all-assign-shift")
    Call<GetAssignShiftsResponse> AllAssignShiftEmp(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("/api/get-all-assign-shift")
    Call<GetAssignShiftsResponse> AllAssignShiftByDate(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("date") String date
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/api/get-today-assign-shift")
    Call<TodayAssignShiftsResponse> TodayAssignShift(
            @Header("Authorization") String token
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST()
    Call<CancelShiftResponse> CancelShift(
            @Url String url,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("/api/change-password")
    Call<ChangePassResponse> ChangePass(
            @Header("Authorization") String token,
            @Field("current_password") String currentPass,
            @Field("new_password") String newPass,
            @Field("password_confirmation") String confirmPass
    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/api/get-timesheet-data")
    Call<TimeSheetResponse> TimeSheetManager(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("/api/get-timesheet-data")
    Call<TimeSheetResponse> TimeSheetEmployee(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @Multipart
    @POST("/api/change-image")
    Call<ChangePassResponse> ChangeImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("/api/shifts")
    Call<AddShiftResponse> AddShift(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("start_time") String startTime,
            @Field("end_time") String endTime
    );

    @FormUrlEncoded
    @POST("/api/wards")
    Call<AddWardResponse> AddWard(
            @Header("Authorization") String token,
            @Field("name") String name
    );

    @DELETE()
    Call<CancelShiftResponse> DeleteShift(
            @Header("Authorization") String token,
            @Url String url
    );

    @FormUrlEncoded
    @POST()
    Call<UpdateShiftResponse> UpdateShift(
            @Header("Authorization") String token,
            @Url String url,
            @Field("name") String name,
            @Field("start_time") String startTime,
            @Field("end_time") String endTime
    );

    @DELETE()
    Call<CancelShiftResponse> DeleteWard(
            @Header("Authorization") String token,
            @Url String url
    );

    @FormUrlEncoded
    @POST()
    Call<AddWardResponse> UpdateWard(
            @Header("Authorization") String token,
            @Url String url,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("/api/send-email-verification-code")
    Call<VerifyEmailResponse> VerifyEmail(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("/api/recover-user-password")
    Call<RecoverPasswordResponse> RecoverPassword(
            @Field("email") String email,
            @Field("new_password") String password,
            @Field("password_confirmation") String confirmPass
    );

}
