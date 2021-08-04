package com.example.employees.viewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.employees.LoginActivity;
import com.example.employees.R;
import com.example.employees.model.RegisterUser;
import com.example.employees.network.callingApi.RegApi;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> confirm_password = new MutableLiveData<>();

    private RegisterUser registerUser;
    private Context context;

    public RegisterViewModel(RegisterUser registerUser, Context context) {
        this.registerUser = registerUser;
        this.context = context;
    }

    public void onSignUpClick(){
        registerUser.setEmail(email.getValue());
        registerUser.setPassword(password.getValue());
        registerUser.setConfirm_password(confirm_password.getValue());

        Log.d("me_s", "onSignUpClick: email_value: "+email.getValue());
        Log.d("me_s", "onSignUpClick: email_register: "+registerUser.getEmail());

        if (!registerUser.isValidEmail()){
            Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
        }
        else if (!registerUser.isValidPassword()){
            Toast.makeText(context, R.string.pass_length_warning, Toast.LENGTH_SHORT).show();
        }
        else if (!registerUser.isPasswordMatched()){
            Toast.makeText(context, R.string.pass_not_matched, Toast.LENGTH_SHORT).show();
        }
        else {
//            RegApi regApi = new RegApi(
//                    "",
//                    "",
//                    email.getValue()
//                    ,password.getValue(),
//                    confirm_password.getValue(),
//                    null,
//                    "",
//                    "",
//                    "",
//                    "1",
//                    context
//            );
//            regApi.registerEmployee();
        }
    }

    public void onLoginClick(){
        context.startActivity(new Intent(this.context, LoginActivity.class));
    }
}
