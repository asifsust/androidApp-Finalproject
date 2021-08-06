package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.employees.databinding.ActivityMainBinding;
import com.example.employees.network.callingApi.LogoutApi;
import com.example.employees.others.GlobalMethods;
import com.example.employees.others.GlobalValues;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.util.Objects;

public class EmployeeHomeActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getName();
    private ActivityMainBinding binding;
    private NavController navController;
    private GlobalMethods globalMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        globalMethods = new GlobalMethods(this);
        setupNavigation();
        setUserDataInDrawer();

        binding.drawer.layoutShare.setOnClickListener(this);
        binding.drawer.layoutRate.setOnClickListener(this);
        binding.drawer.layoutLogout.setOnClickListener(this);
        binding.drawer.imageCloseDrawer.setOnClickListener(this);
        binding.drawer.settingLayout.setOnClickListener(this);
        binding.drawer.layoutAbout.setOnClickListener(this);
    }

    private void setUserDataInDrawer() {
        binding.drawer.textName.setText(globalMethods.getUserName());
        binding.drawer.textRole.setText(globalMethods.getUserRole());
        setImage();
    }

    private void setImage() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        DisplayImageOptions options = new DisplayImageOptions.Builder()

                // stub image will display when your image is loading
                .showStubImage(R.drawable.default_pro_img)

                // below image will be displayed when the image url is empty
                .showImageForEmptyUri(R.drawable.default_pro_img)

                // cachememory method will caches the image in users external storage
                .cacheInMemory()

                // cache on disc will caches the image in users internal storage
                .cacheOnDisc()

                // build will build the view for displaying image..
                .build();

        String imageUrl = GlobalValues.BASE_URL_IMAGE+globalMethods.getUserImage();
        imageLoader.displayImage(imageUrl, binding.drawer.imageProfile, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d(TAG, "onLoadingStarted: loading started");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d(TAG, "onLoadingFailed: loading failed: "+failReason.toString());
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d(TAG, "onLoadingComplete: loading complete");
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.d(TAG, "onLoadingCancelled: loading canceled");
            }
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.settingLayout){
            navController.navigate(R.id.action_employeesFragment_to_settingFragment);
        }
        if (v.getId() == R.id.layoutLogout){
            LogoutApi logoutApi = new LogoutApi(EmployeeHomeActivity.this);
            logoutApi.LogoutUser();
        }
//        if (v.getId() == R.id.imageCloseDrawer){
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//        }
        if (v.getId() == R.id.layoutShare){
            globalMethods.shareApp();
        }
        if (v.getId() == R.id.layoutRate){
            globalMethods.rateApp();
        }
        if (v.getId() == R.id.layoutAbout){
            globalMethods.aboutDialog();
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }
}