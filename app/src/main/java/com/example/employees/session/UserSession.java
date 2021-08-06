package com.example.employees.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.employees.model.login.Employee;
import com.example.employees.model.login.User;
import com.google.gson.Gson;


public class UserSession {
    private static final String USER_DATA = "user_data";
    private static final String EMPLOYEE_DATA = "employee_data";
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public String SHARED_PREFERENCE_NAME = "USER_SESSION";
    public String LOGIN_SESSION = "login_session";
    public String ROLE_ID = "role_id";
    public String ROLE = "role";
    public String TOKEN = "token";
    public String ADMIN_EMAIL = "admin_email";
    public String ADMIN_PASS = "admin_pass";
    public String IMAGE_URI = "image_uri";

    public UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    public void clearUserSession(){
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setLoginStatus(boolean status){
        editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_SESSION,status);
        editor.apply();
    }

    public boolean getLoginStatus(){
        return sharedPreferences.getBoolean(LOGIN_SESSION,false);
    }

    public void setRoleId(int id){
        editor = sharedPreferences.edit();
        editor.putInt(ROLE_ID,id);
        editor.apply();
    }

    public int getRoleId(){
        return sharedPreferences.getInt(ROLE_ID,1);
    }

    public void setRole(String role){
        editor = sharedPreferences.edit();
        editor.putString(ROLE,role);
        editor.apply();
    }

    public String getRole(){
        return sharedPreferences.getString(ROLE,"");
    }

    public void setToken(String token){
        editor = sharedPreferences.edit();
        editor.putString(TOKEN,token);
        editor.apply();
    }

    public String getToken(){
        return sharedPreferences.getString(TOKEN,"");
    }

    public void setAdminData(String email,String pass){
        editor = sharedPreferences.edit();
        editor.putString(ADMIN_EMAIL,email);
        editor.putString(ADMIN_PASS,pass);
        editor.apply();
    }

    public String getAdminEmail(){
        return sharedPreferences.getString(ADMIN_EMAIL,"");
    }

    public String getAdminPass(){
        return sharedPreferences.getString(ADMIN_PASS,"");
    }

    public void setEmployeeDataUser(User user){
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(USER_DATA,json);
        editor.apply();
    }

    public String getEmployeeDataUser(){
        return sharedPreferences.getString(USER_DATA,"");
    }

    public void setEmployeeDataEmp(Employee emp){
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(emp);
        editor.putString(EMPLOYEE_DATA,json);
        editor.apply();
    }

    public void saveLocalImage(String uri){
        editor = sharedPreferences.edit();
        editor.putString(IMAGE_URI,uri);
        editor.apply();
    }

    public String getLocalImage(){
        return sharedPreferences.getString(IMAGE_URI,"");
    }

    public String getEmployeeDataEmp(){
        return sharedPreferences.getString(EMPLOYEE_DATA,"");
    }
}
