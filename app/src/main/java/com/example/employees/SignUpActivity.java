package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.employees.databinding.ActivitySignUpBinding;
import com.example.employees.model.RegisterUser;
import com.example.employees.viewModel.RegisterViewModel;
import com.example.employees.viewModel.factory.RegisterViewModelFactory;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActivitySignUpBinding newBinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        RegisterViewModel registerViewModel = ViewModelProviders.of(this,new RegisterViewModelFactory(new RegisterUser(),this)).get(RegisterViewModel.class);
        newBinding.setRegister(registerViewModel);

    }
}