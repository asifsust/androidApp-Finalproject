package com.example.employees.view.fragment.employee;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.R;
import com.example.employees.databinding.FragmentEmployeeProfileBinding;
import com.example.employees.model.login.Employee;
import com.example.employees.model.login.User;
import com.example.employees.others.GlobalMethods;
import com.example.employees.others.GlobalValues;
import com.example.employees.session.UserSession;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class EmployeeProfileFragment extends Fragment {

    private final String TAG = this.getClass().getName();
    private FragmentEmployeeProfileBinding binding;
    private User user;
    private Employee employee;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEmployeeProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfileData();
        setData();
    }

    private void getProfileData() {
        UserSession userSession = new UserSession(requireContext());
        Gson gson = new Gson();
        user = gson.fromJson(userSession.getEmployeeDataUser(), User.class);
        employee = gson.fromJson(userSession.getEmployeeDataEmp(),Employee.class);
    }

    private void setData() {
        if (user != null){
            binding.textUserId.setText(String.valueOf(user.getUserId()));
            binding.textUserName.setText(user.getName());
            binding.textEmail.setText(user.getEmail());
            binding.textMobile.setText(user.getMobile());
            binding.textRole.setText(user.getRole());
        }
        if (employee != null){
            binding.textDateOfBirth.setText(GlobalMethods.changeDateFormat(employee.getDateOfBirth()));
            binding.textHireDate.setText(GlobalMethods.changeDateFormat(employee.getJoiningDate()));

            if (employee.getImage() != null) setImage();
        }

    }

    private void setImage() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(requireContext()));
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

        String imageUrl = GlobalValues.BASE_URL_IMAGE+employee.getImage();
        imageLoader.displayImage(imageUrl, binding.circleImageProfile, options, new ImageLoadingListener() {
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
}