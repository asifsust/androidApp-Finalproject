package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.employees.databinding.ActivityVerifyEmailBinding;
import com.example.employees.network.callingApi.VerifyEmailApi;
import com.example.employees.others.GlobalMethods;

public class VerifyEmailActivity extends AppCompatActivity {

    private ActivityVerifyEmailBinding binding;
    private Context context;
    private String responseCode;
    private String enteredCode;
    private String email;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        binding.buttonSubmit.setOnClickListener(v -> {
            if (binding.buttonSubmit.getTag().equals("submitEmail")){
                email = binding.edtEmail.getText().toString();
                if (GlobalMethods.emptyToast(email,getString(R.string.enter_email_address),context)
                        && GlobalMethods.isValidEmail(email,context)
                ){
                    VerifyEmailApi api = new VerifyEmailApi(context, email, verificationCode -> {
                        responseCode = String.valueOf(verificationCode);
                        binding.layoutEmail.setVisibility(View.GONE);
                        binding.buttonCancel.setVisibility(View.GONE);
                        binding.layoutCode.setVisibility(View.VISIBLE);
                        binding.layoutSentTo.setVisibility(View.VISIBLE);
                        binding.textEmail.setText(email);
                        binding.buttonSubmit.setTag("submitCode");
                    });
                    api.verifyEmail();
                }
            }else {
                enteredCode = binding.edtCode.getText().toString();
                if (GlobalMethods.emptyToast(enteredCode,"Enter code",context)){
                    if (enteredCode.equals(responseCode)){
                        Intent intent = new Intent(context,ResetPasswordActivity.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(context, "Did not matched the code", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        binding.buttonCancel.setOnClickListener(v -> finish());
    }
}