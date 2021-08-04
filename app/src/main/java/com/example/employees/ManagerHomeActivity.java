package com.example.employees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.employees.databinding.ActivityManagerHomeBinding;
import com.example.employees.network.callingApi.LogoutApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class ManagerHomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityManagerHomeBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigation();
        findViewById(R.id.layoutShare).setOnClickListener(this);
        findViewById(R.id.layoutRate).setOnClickListener(this);
        findViewById(R.id.layoutLogout).setOnClickListener(this);
        findViewById(R.id.imageCloseDrawer).setOnClickListener(this);

    }

    private void setupNavigation() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, binding.drawerLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_employee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_employee) {   //this item has your app icon
            navController.navigate(R.id.action_employeesFragment_to_addNewEmployeeFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = new EmployeesFragment();
                switch (item.getItemId()){
                    case R.id.employeesFragment:
                        selectedFragment = new EmployeesFragment();
                        //binding.textManagerToolbarTitle.setText(R.string.employees);
                        break;

                    case R.id.shiftsFragment:
                        selectedFragment = new ShiftsFragment();
                        //binding.textManagerToolbarTitle.setText(R.string.shifts);
                        break;

                    case R.id.timesheetsFragment:
                        selectedFragment = new TimesheetsFragment();
                        //binding.textManagerToolbarTitle.setText(R.string.timesheets);
                        break;

                    case R.id.managerProfileFragment:
                        selectedFragment = new ManagerProfileFragment();
                        //binding.textManagerToolbarTitle.setText(R.string.profile);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,selectedFragment).commit();
                return true;
            };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layoutShare){
            shareApp();
        }
        if (v.getId() == R.id.layoutRate){
            rateApp();
        }
        if (v.getId() == R.id.imageCloseDrawer){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (v.getId() == R.id.layoutLogout){
            LogoutApi logoutApi = new LogoutApi(ManagerHomeActivity.this);
            logoutApi.LogoutUser();
        }
    }

    private void rateApp() {
        try
        {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="+getPackageName())));
        }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out this awesome app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}