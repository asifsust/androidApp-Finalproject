package com.example.employees.others;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;

import com.example.employees.BuildConfig;
import com.example.employees.ManagerHomeActivity;
import com.example.employees.R;
import com.example.employees.databinding.AboutAppDialogBinding;
import com.example.employees.model.login.Employee;
import com.example.employees.model.login.User;
import com.example.employees.session.UserSession;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class GlobalMethods {
    public String TAG = this.getClass().getName();
    private Context context;
    private Employee employee;
    private User user;
    private UserSession userSession;

    public GlobalMethods() {}
    public GlobalMethods(Context context){
        this.context = context;
        userSession = new UserSession(context);
        Gson gson = new Gson();
        employee = gson.fromJson(userSession.getEmployeeDataEmp(),Employee.class);
        user = gson.fromJson(userSession.getEmployeeDataUser(),User.class);
    }

    public static boolean editTextValidator(String value, TextView warningTV, LinearLayout layout, String type){
        if (type.equals("user_id") && value.length()<6){
            layout.setVisibility(View.VISIBLE);
            warningTV.setText(R.string.user_id_must_be_six_digit);
            return false;
        }

        if (type.equals("password") && (value.length()<8 || value.length()>30)){
            layout.setVisibility(View.VISIBLE);
            warningTV.setText(R.string.your_password_must_be_greater_then_8_and_less_then_30);
            return false;
        }

        return true;
    }

    public static boolean matchPassword(String password, String confirm_password, Context context){
        if (!password.equals(confirm_password)){
            Toast.makeText(context, R.string.password_not_matched, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean isValidEmail(String email,Context context) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean emptyField(String value,LinearLayout layout,TextView textView){
        Log.d("global_method", "emptyField: activated");
        if (TextUtils.isEmpty(value)){
            layout.setVisibility(View.VISIBLE);
            textView.setText(R.string.This_field_can_not_be_empty);
            return false;
        }else layout.setVisibility(View.GONE);

        return true;
    }

    public static boolean phoneNumberLength(Context context,String mobile){
        if (mobile.length() != 11){
            Toast.makeText(context, "Mobile number length must be 11", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean emptyToast(String value,String message,Context context){
        if (TextUtils.isEmpty(value) || value.equals("")){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        return dateFormat.format(date);
    }

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss",Locale.UK);
        return dateFormat.format(c.getTime());

    }

    public static String getNextDate(int dayValue){
        Date date = new Date();
        Date newDate = new Date(date.getTime() + dayValue*24*60*60*1000L); // 7 * 24 * 60 * 60 * 1000
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        Log.d("my_date", "getNextDate: date: "+dateFormat.format(newDate));
        return dateFormat.format(newDate);
    }

    public static String getNextWeek(int weekValue){
        Date date = new Date();
        Date newDate = new Date(date.getTime() + weekValue*24*60*60*1000L); // 1 * 24 * 60 * 60 * 1000 = 1 day
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.UK);
        Log.d("my_week", "getNextDate: week: "+dateFormat.format(newDate));
        return dateFormat.format(newDate);
    }

    public static void refreshCurrentFragment(NavController navController) {
        int id = Objects.requireNonNull(navController.getCurrentDestination()).getId();
        navController.popBackStack(id,true);
        navController.navigate(id);
    }

    public static void showAddMenu(boolean showMenu){
        if (ManagerHomeActivity.menu != null){
            MenuItem item1 = ManagerHomeActivity.menu.findItem(R.id.menu_add_employee);
            MenuItem item2 = ManagerHomeActivity.menu.findItem(R.id.menu_all_shift);
            MenuItem item3 = ManagerHomeActivity.menu.findItem(R.id.menu_all_ward);
            MenuItem item4 = ManagerHomeActivity.menu.findItem(R.id.menu_add_shift);
            MenuItem item = ManagerHomeActivity.menu.findItem(R.id.menu_add_ward);
            item1.setVisible(showMenu);
            item2.setVisible(showMenu);
            item3.setVisible(showMenu);
            item4.setVisible(showMenu);
            item.setVisible(showMenu);
        }
    }

    public static void showAddShift(boolean showAddShift){
        if (ManagerHomeActivity.menu != null){
            MenuItem item1 = ManagerHomeActivity.menu.findItem(R.id.menu_add_shift);
            item1.setVisible(showAddShift);
        }
    }

    public static void showAddWard(boolean showAddShift){
        if (ManagerHomeActivity.menu != null){
            MenuItem item1 = ManagerHomeActivity.menu.findItem(R.id.menu_add_ward);
            item1.setVisible(showAddShift);
        }
    }

    public static String changeDateFormat(String oldDate){
        Date myDate;
        String newDate = null;
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        try{
            myDate = oldFormat.parse(oldDate);
            newDate = newFormat.format(Objects.requireNonNull(myDate));
        }catch (Exception e){
            Log.d("date_format", "changeDateFormat: date issue: "+e.getMessage());
        }

        return newDate;
    }

    public static String changeDateFormatYMD(String oldDate){
        Date myDate;
        String newDate = null;
        SimpleDateFormat oldFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        try{
            myDate = oldFormat.parse(oldDate);
            newDate = newFormat.format(Objects.requireNonNull(myDate));
        }catch (Exception e){
            Log.d("date_format", "changeDateFormat: date issue: "+e.getMessage());
        }

        return newDate;
    }

    public static String changeTimeFormat(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.UK);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.UK);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String changeTimeFormatTo00(String time) {
        String outputPattern = "HH:mm:ss";
        String inputPattern = "HH:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.UK);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.UK);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getEmpUserId(){
        return String.valueOf(employee.getUserId());
    }

    public String getUserImage(){
        if (employee!=null)
        return employee.getImage();
        return "";
    }

    public String getUserName(){
        if (user!=null) return user.getName();
        return "Not found";
    }

    public String getUserRole(){
        if (user!=null) return user.getRole();
        return "Not found";
    }

    public static boolean isValidTimeForShift(String initialTime, String finalTime,
                                              String currentTime) {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        if (initialTime.matches(reg) && finalTime.matches(reg) &&
                currentTime.matches(reg))
        {
            Date inTime=null,checkTime=null,finTime=null;

            try{
                inTime = new SimpleDateFormat("HH:mm:ss",Locale.UK).parse(initialTime);
                checkTime = new SimpleDateFormat("HH:mm:ss",Locale.UK).parse(currentTime);
                finTime = new SimpleDateFormat("HH:mm:ss",Locale.UK).parse(finalTime);
            }catch (Exception e){
                e.printStackTrace();
            }

            //Start Time
            //all times are from java.util.Date
            Calendar calendar1 = Calendar.getInstance();
            assert inTime != null;
            calendar1.setTime(inTime);
            //Current Time

            Calendar calendar3 = Calendar.getInstance();
            assert checkTime != null;
            calendar3.setTime(checkTime);

            //End Time
            Calendar calendar2 = Calendar.getInstance();
            assert finTime != null;
            calendar2.setTime(finTime);


            Date actualTime = calendar3.getTime();

            if (checkTime.before(calendar1.getTime())){
                return true;
            }else {
                if (finalTime.compareTo(initialTime) < 0 && checkTime.after(calendar1.getTime())){
                    return true;
                }else {
                    if (checkTime.before(calendar2.getTime())){
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public void rateApp() {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="+context.getPackageName())));
        }
        catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out this awesome app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public void aboutDialog() {
        Dialog dialog = new Dialog(context);
        AboutAppDialogBinding dialogBinding = AboutAppDialogBinding.inflate(LayoutInflater.from(context),null,false);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogBinding.imageClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
