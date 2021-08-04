package com.example.employees.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.R;
import com.example.employees.databinding.FragmentEmployeeRoasterBinding;
import com.example.employees.model.employees.EmployeeData;
import com.example.employees.network.callingApi.EmployeesApi;

import java.util.ArrayList;

public class EmployeeRoasterFragment extends Fragment {

    private FragmentEmployeeRoasterBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeeRoasterBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(binding.getRoot());

        binding.availableBankDutiesLayout.setOnClickListener(v ->
                navController.navigate(R.id.action_employeeRoasterFragment_to_availableBankDutiesFragment)
        );

        binding.profileLayout.setOnClickListener(v ->
                navController.navigate(R.id.action_employeeRoasterFragment_to_employeeProfileFragment)
        );

        binding.viewRoasterLayout.setOnClickListener(v ->
                navController.navigate(R.id.action_employeeRoasterFragment_to_personalRoasterFragment));

        binding.timeSheetsLayout.setOnClickListener(v ->
                navController.navigate(R.id.action_employeeRoasterFragment_to_employeeTimeSheetsFragment));

    }
}