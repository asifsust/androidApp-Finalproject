package com.example.employees.viewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.employees.ForgetPasswordActivity;
import com.example.employees.R;
import com.example.employees.SignUpActivity;
import com.example.employees.model.User;
import com.example.employees.network.callingApi.LoginApi;

public class LoginViewModel extends ViewModel {
    private final String TAG = this.getClass().getName();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private final User user;
    private Context context;

    public LoginViewModel(User user, Context context) {
        this.user = user;
        this.context = context;
    }

    public void onLoginClick(){
        user.setEmail(email.getValue());
        user.setPassword(password.getValue());

        if (!user.isValidEmail()){
            Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
        }
        else if(!user.isValidPassword()){
            Toast.makeText(context, R.string.pass_length_warning, Toast.LENGTH_SHORT).show();
        }
        else {
            LoginApi loginApi = new LoginApi(user.getEmail(),user.getPassword(),context);
            loginApi.loginUser();
        }
    }

    public void onSignUpClick(){
        context.startActivity(new Intent(this.context, SignUpActivity.class));
    }

    public void onForgotPasswordClick(){
        context.startActivity(new Intent(this.context, ForgetPasswordActivity.class));
    }

    public void onRoleCheckedChange(RadioGroup radioGroup, int id){
        int checkedId =  radioGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.radioButtonEmployee) Log.d(TAG, "onRoleCheckedChange: employee");
        if (checkedId == R.id.radioButtonManager) Log.d(TAG, "onRoleCheckedChange: manager");
    }
}
