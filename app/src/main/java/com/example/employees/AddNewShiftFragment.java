package com.example.employees;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.databinding.FragmentAddNewShiftBinding;

import java.util.Calendar;

public class AddNewShiftFragment extends Fragment {

    private FragmentAddNewShiftBinding binding;
    private int currentYear,currentMonth, currentDayOfMonth;
    private int mHour,mMinute;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddNewShiftBinding.inflate(inflater,container,false);
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        binding.buttonSelectDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),R.style.DialogTheme,
                    (view, year, month, dayOfMonth) -> binding.buttonSelectDate.setText(
                            new StringBuilder().append(dayOfMonth).append("/")
                                    .append(month).append("/").append(year)
                    ),currentYear,currentMonth,currentDayOfMonth);

            datePickerDialog.show();
        });

        binding.buttonSelectTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),R.style.DialogTheme, (view, hourOfDay, minute) -> {
                String amPm;
                if (hourOfDay>=12){
                    if (hourOfDay != 12) hourOfDay = hourOfDay - 12;
                    amPm = "pm";
                }else {
                    if (hourOfDay == 0) hourOfDay=12;
                    amPm = "am";
                }

                binding.buttonSelectTime.setText(
                        new StringBuilder().append(hourOfDay).append(":").append(minute).append(" ").append(amPm)
                );
            },mHour,mMinute,false);

            timePickerDialog.show();
        });


        return binding.getRoot();
    }
}