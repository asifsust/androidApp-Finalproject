package com.example.employees.others;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employees.R;

public class GlobalMethods {
    public String TAG = this.getClass().getName();
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

    public static boolean emptyToast(String value,String message,Context context){
        if (TextUtils.isEmpty(value)){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
