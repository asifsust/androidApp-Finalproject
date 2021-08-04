package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.example.employees.databinding.ActivityMainBinding;

import java.util.Objects;

public class EmployeeHomeActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigation();

        binding.drawer.imageCloseDrawer.setOnClickListener(v -> binding.drawerLayout.closeDrawer(GravityCompat.START));
        binding.drawer.settingLayout.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.action_employeesFragment_to_settingFragment);
        });
    }

    private void setupNavigation() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navController = Navigation.findNavController(this, R.id.employeeFragmentContainer);
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,binding.drawerLayout);
    }
}