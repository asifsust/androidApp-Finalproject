package com.example.employees.view.fragment.manager;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.R;
import com.example.employees.ResetPasswordActivity;
import com.example.employees.databinding.FragmentManagerProfileBinding;
import com.example.employees.model.login.Employee;
import com.example.employees.model.login.User;
import com.example.employees.network.callingApi.ChangeImageApi;
import com.example.employees.others.GlobalMethods;
import com.example.employees.others.GlobalValues;
import com.example.employees.session.UserSession;
import com.example.employees.utils.FileUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class ManagerProfileFragment extends Fragment {

    private static final int REQUEST_CODE = 111;
    private final String TAG = this.getClass().getName();
    private FragmentManagerProfileBinding binding;
    private UserSession userSession;
    private Context context;
    private Employee employee;
    private User user;

    private File file;

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

        binding = FragmentManagerProfileBinding.inflate(inflater,container,false);
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

        init();
        setData();

        if (permission()) {
            Log.d(TAG, "onViewCreated: permitted");
        } else {
            RequestPermission_Dialog();
        }

        binding.buttonChangePass.setOnClickListener(v -> {
            GlobalValues.isManager = true;
            Intent intent = new Intent(context, ResetPasswordActivity.class);
            intent.putExtra("current_pass",userSession.getAdminPass());
            startActivity(intent);
        });

        binding.textChangeImage.setOnClickListener(v -> chooseImageFromGallery());
    }

    /**
     *
     * @return
     */
    public boolean permission() {
        if (SDK_INT >= Build.VERSION_CODES.R) { // R is Android 11
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED
                    && read == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     *
     */
    public void RequestPermission_Dialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", context.getPackageName())));
                startActivityForResult(intent, 2000);
            } catch (Exception e) {
                Intent obj = new Intent();
                obj.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(obj, 2000);
            }
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (storage && read) {
                    //next activity
                } else {
                    //show msg kai permission allow nahi havai
                }
            }
        }
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    //msg show karo
                    //move to next activity
                } else {

                }
            }
        }
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult.launch(intent);

    }

    /**
     *
     */
    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {

                        Intent data = result.getData();
                        Uri imageUri = Objects.requireNonNull(data).getData();

                        int orientation = getOrientation(requireContext(), imageUri); //get orientation in degree
                        Log.d(TAG,String.valueOf(orientation));

                        try {
                            file = FileUtil.from(requireContext(), imageUri);
                            Log.d(TAG, "onActivityResult: filePath: "+ file.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "onActivityResult: url: "+ imageUri.toString());
                        uploadImageToServer();

                        Bitmap bitmap = null;
                        try {
                            bitmap = rotateImage(requireContext(), imageUri,orientation);
                            binding.imageMangerProfile.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        userSession.saveLocalImage(saveToInternalStorage(bitmap));


                    }
                }
            }
    );

    /**
     *
     */
    private void uploadImageToServer() {
        MultipartBody.Part filePart;
        if (file != null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            filePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }else filePart = null;

        ChangeImageApi api = new ChangeImageApi(context,filePart);
        api.changeImage();
    }

    /**
     *
     * @param context
     * @param uri
     * @param orientation
     * @return
     * @throws IOException
     */
    public static Bitmap rotateImage(Context context, Uri uri, int orientation) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    /**
     *
     * @param context
     * @param uri
     * @return
     */
    public static int getOrientation(Context context, Uri uri) {

        int rotate = 0;

        try {

            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");

            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            FileInputStream input = new FileInputStream(fileDescriptor);

            File tempFile = File.createTempFile("exif", "tmp");

            FileOutputStream output = new FileOutputStream(tempFile.getPath());

            int read;

            byte[] bytes = new byte[4096];

            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }

            input.close();
            output.close();

            ExifInterface exif = new ExifInterface(tempFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            Log.d("TAG", e.getLocalizedMessage());
        }

        return rotate;
    }

    /**
     *
     */
    private void setData() {
        if (user != null){
            binding.textName.setText(user.getName());
            binding.textMobile.setText(user.getMobile());
            binding.textUserId.setText(String.valueOf(user.getUserId()));
            binding.textRole.setText(user.getRole());
            binding.textEmail.setText(user.getEmail());
        }
        if (employee != null){
            binding.textDateOfBirth.setText(employee.getDateOfBirth());
            if (!userSession.getLocalImage().equals("")) {
                loadImageFromStorage(userSession.getLocalImage());
            }
            else {
                if (employee.getImage() != null) setImage();
            }
        }
    }

    /**
     *
     * @param bitmapImage
     * @return
     */
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(fos).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    /**
     *
     * @param path
     */
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            binding.imageMangerProfile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

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

        Log.d(TAG, "setImage: path"+employee.getImage());
        String imageUrl = GlobalValues.BASE_URL_IMAGE+employee.getImage();
        imageLoader.displayImage(imageUrl, binding.imageMangerProfile, options, new ImageLoadingListener() {
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

            /**
             *
             * @param imageUri
             * @param view
             * @param loadedImage
             */
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d(TAG, "onLoadingComplete: loading complete");
            }

            /**
             *
             * @param imageUri
             * @param view
             */
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.d(TAG, "onLoadingCancelled: loading canceled");
            }
        });
    }

    /**
     *
     */
    private void init() {
        GlobalMethods.showAddMenu(false);
        context = requireContext();
        userSession = new UserSession(context);

        Gson gson = new Gson();
        user = gson.fromJson(userSession.getEmployeeDataUser(), User.class);
        employee = gson.fromJson(userSession.getEmployeeDataEmp(),Employee.class);
    }
}