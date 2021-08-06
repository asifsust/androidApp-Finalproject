package com.example.employees.view.fragment.manager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.employees.others.CustomDatePickerDialog;
import com.example.employees.R;
import com.example.employees.databinding.FragmentAddNewEmployeeBinding;
import com.example.employees.model.employees.EmployeeData;
import com.example.employees.network.callingApi.RegApi;
import com.example.employees.network.callingApi.UpdateEmployeeApi;
import com.example.employees.others.GlobalMethods;
import com.example.employees.utils.FileUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AddNewEmployeeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getName();
    private FragmentAddNewEmployeeBinding binding;
    private static final String[] paths = {"Select", "RGN", "HCS"};

    private int currentYear,currentMonth, currentDayOfMonth;
    private String birthDate,joiningDate;
    private String employeeRole;
    private File file;
    private NavController navController;
    private EmployeeData data;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddNewEmployeeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalMethods.showAddMenu(false);
        navController = Navigation.findNavController(binding.getRoot());

        Bundle arguments = getArguments();

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        int minYear = 1930;
        int minMonth = 0;
        int minDay = 1;
        int maxYear = 2006;
        int maxMonth = 11;
        int maxDay = 31;

        binding.buttonSelectBirthDate.setOnClickListener(v -> {
            DatePickerDialog myDatePicker = new CustomDatePickerDialog(requireContext(), (view12, year, month, dayOfMonth) -> {
                month++;
                birthDate = year + "-" + month + "-" + dayOfMonth;
                //Log.d(TAG, "onViewCreated: birth: "+birthDate);
                binding.buttonSelectBirthDate.setText(
                        new StringBuilder().append(dayOfMonth).append("-")
                                .append(month).append("-").append(year)
                );
            },minYear,minMonth,minDay,maxYear,maxMonth,maxDay);

            myDatePicker.show();

        });

        binding.buttonSelectJoiningDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), R.style.DialogTheme,
                    (view1, year, month, dayOfMonth) -> {
                        month++;
                        joiningDate = year + "-" + month + "-" + dayOfMonth;
                        binding.buttonSelectJoiningDate.setText(
                                new StringBuilder().append(dayOfMonth).append("-")
                                        .append(month).append("-").append(year)
                        );
                    },currentYear,currentMonth,currentDayOfMonth);

            datePickerDialog.show();
        });

        binding.profileLayout.setOnClickListener(v -> chooseImageFromGallery());

        binding.buttonAdd.setOnClickListener(v -> {
            if (binding.buttonAdd.getText().equals(getString(R.string.add))) addEmployeeValidation();
            else updateEmployee();
            }
        );

        setRoleSpinner();

        if (arguments != null && arguments.containsKey("object")){
            data = (EmployeeData) requireArguments().getSerializable("object");
            setToolbarTitle("Update Employee");
            binding.layoutPasswordField.setVisibility(View.GONE);
            binding.layoutConfirmPasswordField.setVisibility(View.GONE);
        }else setToolbarTitle("Add Employee");


        if (data != null){
            setDataForUpdate();
        }
    }

    private void setRoleSpinner() {
        spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_layout,
                R.id.textSpinner,
                paths
        );

        binding.mySpinner.setAdapter(spinnerAdapter);
        binding.mySpinner.setOnItemSelectedListener(this);
    }

    private void updateEmployee() {
        String userId = binding.edtUserId.getText().toString();
        String name = binding.edtName.getText().toString();
        String date_of_birth = GlobalMethods.changeDateFormatYMD(binding.buttonSelectBirthDate.getText().toString());
        String email = binding.edtEmail.getText().toString();
        String mobile = binding.edtMobile.getText().toString();
        String joining_date = GlobalMethods.changeDateFormatYMD(binding.buttonSelectJoiningDate.getText().toString());

        MultipartBody.Part filePart;
        if (file != null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            filePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }else filePart = null;


        if (TextUtils.isEmpty(userId)) userId = "";

        String role_id;
        if (employeeRole.equals(getString(R.string.RGN))) role_id = "2";
        else role_id = "3";

        UpdateEmployeeApi updateEmployeeApi = new UpdateEmployeeApi(
                requireContext(),
                stringToBody(name),
                stringToBody(mobile),
                stringToBody(email),
                filePart,
                stringToBody(date_of_birth),
                stringToBody(joining_date),
                stringToBody(userId),
                stringToBody(role_id),
                navController,
                String.valueOf(data.getId()),
                String.valueOf(data.getEmployeeId())
        );
        updateEmployeeApi.updateEmployee();

    }

    private void setToolbarTitle(String title) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(title);
    }

    private void setDataForUpdate() {
        binding.edtUserId.setText(String.valueOf(data.getUserId())); binding.edtUserId.setEnabled(false);
        binding.edtName.setText(data.getName());
        binding.buttonSelectBirthDate.setText(GlobalMethods.changeDateFormat(data.getDateOfBirth()));
        binding.buttonSelectJoiningDate.setText(GlobalMethods.changeDateFormat(data.getJoiningDate()));
        binding.edtMobile.setText(data.getMobile());
        binding.edtEmail.setText(data.getEmail());

        int selectionPosition= spinnerAdapter.getPosition(data.getRole());
        binding.mySpinner.setSelection(selectionPosition);

        setImage();
        binding.buttonAdd.setText(R.string.update);
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

        imageLoader.displayImage(data.getImage(), binding.circleImageProfile, options, new ImageLoadingListener() {
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

    private void addEmployeeValidation() {
        String userId = binding.edtUserId.getText().toString();
        String password = binding.edtPassword.getText().toString();
        String confirm_password = binding.edtConfirmPassword.getText().toString();
        String name = binding.edtName.getText().toString();
        String date_of_birth = birthDate;
        String email = binding.edtEmail.getText().toString();
        String mobile = binding.edtMobile.getText().toString();
        String joining_date = joiningDate;

        if (
                GlobalMethods.emptyField(password,binding.layoutPassword,binding.textPassword) &&
                GlobalMethods.emptyField(confirm_password,binding.layoutConfirmPassword,binding.textConfirmPassword) &&
                GlobalMethods.emptyField(name,binding.layoutName,binding.textName) &&
                GlobalMethods.emptyField(email,binding.layoutEmail,binding.textEmail) &&
                GlobalMethods.emptyField(mobile,binding.layoutMobile,binding.textMobile) &&
                GlobalMethods.emptyToast(date_of_birth,getString(R.string.select_birth_date),requireContext()) &&
                GlobalMethods.emptyToast(joining_date,getString(R.string.select_joining_date),requireContext()) &&
                isRoleSelected() &&
                GlobalMethods.editTextValidator(password,binding.textPassword,binding.layoutPassword,"password") &&
                GlobalMethods.matchPassword(password,confirm_password,requireContext()) &&
                GlobalMethods.phoneNumberLength(requireContext(),mobile) &&
                GlobalMethods.isValidEmail(email,requireContext())

        ){
            MultipartBody.Part filePart;
            if (file != null){
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                filePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            }else filePart = null;


            if (TextUtils.isEmpty(userId)) userId = "";

            String role_id;
            if (employeeRole.equals(getString(R.string.RGN))) role_id = "2";
            else role_id = "3";

            RegApi regApi = new RegApi(
                    stringToBody(name),
                    stringToBody(mobile),
                    stringToBody(email),
                    stringToBody(password),
                    stringToBody(confirm_password),
                    filePart,
                    stringToBody(date_of_birth),
                    stringToBody(joining_date),
                    stringToBody(userId),
                    stringToBody(role_id),
                    requireContext(),
                    navController
            );
            regApi.registerEmployee();
        }
    }

    public boolean isRoleSelected(){
        if (employeeRole.equals("Select")){
            Toast.makeText(requireContext(), R.string.select_role, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult.launch(intent);

    }

    public RequestBody stringToBody(String string){
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), string);
        return body;
    }

    public RequestBody fileToBody(File imageFile){
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), imageFile);
        return body;
    }

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

                        Bitmap bitmap;
                        try {
                            bitmap = rotateImage(requireContext(), imageUri,orientation);
                            binding.circleImageProfile.setImageBitmap(bitmap);
                            binding.circleImageCamera.setVisibility(View.GONE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
    );

    private byte[] reducingImgSize(Bitmap bitmap) { //return byte
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos); //reduce 25%
        return baos.toByteArray();
    }

    private Bitmap reduceBitmapSize(Bitmap bitmap){ //return bitmap
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return decoded;
    }

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                employeeRole = getString(R.string.select);
                break;

            case 1:
                employeeRole = getString(R.string.RGN);
                break;

            case 2:
                employeeRole = getString(R.string.HCS);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}