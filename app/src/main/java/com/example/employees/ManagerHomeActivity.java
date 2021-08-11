package com.example.employees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.employees.databinding.ActivityManagerHomeBinding;
import com.example.employees.databinding.AddShiftDialogBinding;
import com.example.employees.databinding.AddWardDialogBinding;
import com.example.employees.network.callingApi.AddShiftApi;
import com.example.employees.network.callingApi.AddWardApi;
import com.example.employees.network.callingApi.LogoutApi;
import com.example.employees.others.GlobalMethods;
import com.example.employees.others.GlobalValues;
import com.example.employees.session.UserSession;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Objects;

public class ManagerHomeActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getName();
    private ActivityManagerHomeBinding binding;
    private NavController navController;
    public static Menu menu;
    private Context context;

    private GlobalMethods globalMethods;
    private UserSession userSession;

    private int mHour,mMinute;
    private String startTime,endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userSession = new UserSession(this);
        globalMethods = new GlobalMethods(this);
        context = this;

        Calendar calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR);
        mMinute = calendar.get(Calendar.MINUTE);

        setupNavigation();

        binding.drawer.layoutShare.setOnClickListener(this);
        binding.drawer.layoutRate.setOnClickListener(this);
        binding.drawer.layoutLogout.setOnClickListener(this);
        binding.drawer.imageCloseDrawer.setOnClickListener(this);
        binding.drawer.settingLayout.setOnClickListener(this);
        binding.drawer.layoutAbout.setOnClickListener(this);

        setDataInDrawer();

    }

    private void setDataInDrawer() {
        binding.drawer.textName.setText(globalMethods.getUserName());
        binding.drawer.textRole.setText(globalMethods.getUserRole());
        if (!userSession.getLocalImage().equals("")) {
            loadImageFromStorage(userSession.getLocalImage());
        }
        else  {
            if (globalMethods.getUserImage() != null) setImage();
        }
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            binding.drawer.imageProfile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

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
        ManagerHomeActivity.menu = menu;
        GlobalMethods.showAddShift(false);
        GlobalMethods.showAddWard(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_add_employee){
            navController.navigate(R.id.action_employeesFragment_to_addNewEmployeeFragment);
            return true;
        }
//        if (item.getItemId() == R.id.menu_all_shift){
//            navController.navigate(R.id.action_employeesFragment_to_allShiftsFragment);
//            return true;
//        }
        if (item.getItemId() == R.id.menu_all_ward){
            navController.navigate(R.id.action_employeesFragment_to_allWardsFragment);
            return true;
        }
//        if (item.getItemId() == R.id.menu_add_shift){
//            showAddShiftDialog();
//            return true;
//        }
        if (item.getItemId() == R.id.menu_add_ward){
            showAddWardDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddWardDialog() {
        Dialog dialog = new Dialog(this);
        AddWardDialogBinding wardDialogBinding = AddWardDialogBinding.inflate(LayoutInflater.from(this),null,false);
        dialog.setContentView(wardDialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        wardDialogBinding.buttonAdd.setOnClickListener(v -> {
            String wardName = wardDialogBinding.edtWardName.getText().toString();
            if (GlobalMethods.emptyToast(wardName,"Enter ward name",v.getContext())){
                dialog.dismiss();
                AddWardApi api = new AddWardApi(context,wardName);
                api.addWard();
            }
        });

        dialog.show();
    }

    private void showAddShiftDialog() {
        Dialog dialog = new Dialog(this);
        AddShiftDialogBinding shiftBinding = AddShiftDialogBinding.inflate(LayoutInflater.from(this),null,false);
        dialog.setContentView(shiftBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        shiftBinding.buttonStartTime.setOnClickListener(v -> selectTime(shiftBinding.buttonStartTime));
        shiftBinding.buttonEndTime.setOnClickListener(v -> selectTime(shiftBinding.buttonEndTime));

        shiftBinding.buttonAdd.setOnClickListener(v -> {
            String shiftName = shiftBinding.edtShiftName.getText().toString();
            if (GlobalMethods.emptyToast(shiftName,"Enter shift name",v.getContext())
                && GlobalMethods.emptyToast(startTime,"Choose start time",v.getContext())
                && GlobalMethods.emptyToast(endTime,"Choose end time",v.getContext())
            ){
                dialog.dismiss();
                AddShiftApi api = new AddShiftApi(context,shiftName,startTime,endTime);
                api.addShift();
            }
        });

        dialog.show();
    }

    private void selectTime(AppCompatButton button) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.DialogTheme, (view, hourOfDay, minute) -> {
            String amPm;
            int myHour = hourOfDay;
            if (hourOfDay>=12){
                if (hourOfDay != 12) hourOfDay = hourOfDay - 12;
                amPm = "PM";
            }else {
                if (hourOfDay == 0) hourOfDay=12;
                amPm = "AM";
            }

            String time = hourOfDay+":"+minute+" "+amPm;
            String timeToUpload = myHour+":"+minute+":00";
            button.setText(time);

            if (button.getId() == R.id.buttonStartTime) startTime = timeToUpload;
            else endTime = timeToUpload;

        },mHour,mMinute,true);

        timePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layoutShare){
            globalMethods.shareApp();
        }
        if (v.getId() == R.id.layoutRate){
            globalMethods.rateApp();
        }
//        if (v.getId() == R.id.imageCloseDrawer){
//            binding.drawerLayout.closeDrawer(GravityCompat.START);
//        }
        if (v.getId() == R.id.layoutLogout){
            LogoutApi logoutApi = new LogoutApi(ManagerHomeActivity.this);
            logoutApi.LogoutUser();
        }
        if (v.getId() == R.id.settingLayout){
            navController.navigate(R.id.action_employeesFragment_to_settingFragment);
        }
        if (v.getId() == R.id.layoutAbout){
            globalMethods.aboutDialog();
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }
}