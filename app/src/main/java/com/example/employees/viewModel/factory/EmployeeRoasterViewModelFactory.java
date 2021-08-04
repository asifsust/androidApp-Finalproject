package com.example.employees.viewModel.factory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.employees.viewModel.EmployeeRoasterViewModel;

public class EmployeeRoasterViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Context context;

    public EmployeeRoasterViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new EmployeeRoasterViewModel(context);

    }
}
