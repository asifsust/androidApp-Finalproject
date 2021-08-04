package com.example.employees.viewModel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.employees.model.RegisterUser;
import com.example.employees.viewModel.RegisterViewModel;

public class RegisterViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private RegisterUser registerUser;
    private Context context;

    public RegisterViewModelFactory(RegisterUser registerUser, Context context) {
        this.registerUser = registerUser;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new RegisterViewModel(registerUser,context);
    }
}
