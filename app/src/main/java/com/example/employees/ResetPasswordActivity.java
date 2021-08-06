package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.employees.databinding.ActivityResetPasswordBinding;
import com.example.employees.network.callingApi.ChangePassApi;
import com.example.employees.network.callingApi.RecoverPasswordApi;
import com.example.employees.others.GlobalMethods;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    private Context context;
    private String currentPass;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ResetPasswordActivity.this;
        getPassValue();

        binding.buttonChangePass.setOnClickListener(v -> {
            String newPass = binding.edtNewPass.getText().toString();
            String confirmPass = binding.edtConfirmPass.getText().toString();

            if (GlobalMethods.emptyToast(newPass,getString(R.string.enter_new_pass),context)
                && GlobalMethods.emptyToast(confirmPass,getString(R.string.enter_confirm_pass),context)
                && GlobalMethods.matchPassword(newPass,confirmPass,context)
            ){
                if (TextUtils.isEmpty(email)){
                    ChangePassApi changePassApi = new ChangePassApi(context,currentPass,newPass);
                    changePassApi.changePass();
                }else {
                    RecoverPasswordApi api = new RecoverPasswordApi(context,email,newPass);
                    api.recoverPassword();
                }

            }
        });
    }

    private void getPassValue() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("current_pass"))
            currentPass = getIntent().getStringExtra("current_pass");

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("email"))
            email = getIntent().getStringExtra("email");
    }
}