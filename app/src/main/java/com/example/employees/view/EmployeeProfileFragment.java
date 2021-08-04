package com.example.employees.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.R;
import com.example.employees.databinding.FragmentEmployeeProfileBinding;

public class EmployeeProfileFragment extends Fragment {

    private FragmentEmployeeProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeProfileBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}