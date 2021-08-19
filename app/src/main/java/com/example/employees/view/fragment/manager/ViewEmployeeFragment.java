package com.example.employees.view.fragment.manager;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.R;
import com.example.employees.databinding.FragmentViewEmployeeBinding;
import com.example.employees.model.employees.EmployeeData;
import com.example.employees.others.GlobalMethods;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ViewEmployeeFragment extends Fragment {

    private final String TAG = this.getClass().getName();
    private FragmentViewEmployeeBinding binding;
    private EmployeeData data;
    private NavController navController;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewEmployeeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalMethods.showAddMenu(false);
        navController = Navigation.findNavController(binding.getRoot());
        data = (EmployeeData) requireArguments().getSerializable("object");
        setData();

        binding.buttonAssignShift.setOnClickListener(v ->{
                Bundle bundle = new Bundle();
                bundle.putString("user_id",String.valueOf(data.getId()));
                navController.navigate(R.id.action_viewEmployeeFragment_to_addNewShiftFragment,bundle);
            }
        );

    }

    /**
     *
     */
    private void setData() {
        Log.d(TAG, "setData: birth: "+data.getDateOfBirth());
        Log.d(TAG, "setData: join: "+data.getJoiningDate());
        binding.textUserName.setText(data.getName());
        binding.textDateOfBirth.setText(GlobalMethods.changeDateFormat(data.getDateOfBirth()));
        binding.textEmail.setText(data.getEmail());
        binding.textJoiningDate.setText(GlobalMethods.changeDateFormat(data.getJoiningDate()));
        binding.textMobile.setText(data.getMobile());
        binding.textRole.setText(data.getRole());
        binding.textUserId.setText(String.valueOf(data.getUserId()));

        setImage();
    }

    /**
     *
     */
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

        imageLoader.displayImage(data.getImage(), binding.imageProfile, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d(TAG, "onLoadingStarted: loading started");
            }

            /**
             *
             * @param imageUri
             * @param view
             * @param failReason
             */
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