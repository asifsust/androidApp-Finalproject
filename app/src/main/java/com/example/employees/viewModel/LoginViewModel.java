package com.example.employees.viewModel;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.employees.VerifyEmailActivity;
import com.example.employees.R;
import com.example.employees.SignUpActivity;
import com.example.employees.model.User;
import com.example.employees.network.callingApi.LoginApi;

import java.util.Objects;

public class LoginViewModel extends ViewModel {
    private final String TAG = this.getClass().getName();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Integer> radio_checked = new MutableLiveData<>();

    private final User user;
    private final Context context;


    public LoginViewModel(User user, Context context) {
        this.user = user;
        this.context = context;
        radio_checked.postValue(1);
    }

    public void onLoginClick(){
        user.setEmail(email.getValue());
        user.setPassword(password.getValue());
        int checkedId = radio_checked.getValue();

        if (!user.isValidEmail()){
            Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
        }
        else if(!user.isValidPassword()){
            Toast.makeText(context, R.string.pass_length_warning, Toast.LENGTH_SHORT).show();
        }
        else if (checkedId == 1){
            Toast.makeText(context, R.string.manager_employee, Toast.LENGTH_SHORT).show();
        }
        else if (checkedId == R.id.radioButtonManager && !Objects.equals(email.getValue(), "nhs.manager21@gmail.com") && !Objects.equals(password.getValue(),"admin12345")){
            Toast.makeText(context, R.string.invalid_for_manager, Toast.LENGTH_SHORT).show();
        }
        else if (checkedId == R.id.radioButtonEmployee && Objects.equals(email.getValue(), "nhs.manager21@gmail.com") && Objects.equals(password.getValue(),"admin12345")){
            Toast.makeText(context, R.string.invalid_for_employee, Toast.LENGTH_SHORT).show();
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
        context.startActivity(new Intent(this.context, VerifyEmailActivity.class));
    }
}
