package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.employees.databinding.ActivityLoginBinding;
import com.example.employees.model.User;
import com.example.employees.viewModel.LoginViewModel;
import com.example.employees.viewModel.factory.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActivityLoginBinding newBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        LoginViewModel loginViewModel = ViewModelProviders.of(this,new LoginViewModelFactory(new User(),this)).get(LoginViewModel.class);
        newBinding.setLoginViewModel(loginViewModel);

    }
}