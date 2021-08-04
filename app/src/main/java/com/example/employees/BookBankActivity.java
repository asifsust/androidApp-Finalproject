package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.employees.databinding.ActivityBookBankBinding;

import java.util.Objects;

public class BookBankActivity extends AppCompatActivity {

    private ActivityBookBankBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookBankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        findViewById(R.id.acbBookBank).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(BookBankActivity.this);
//                dialog.setContentView(R.layout.confirm_booking_dialog);
//                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
    }
}