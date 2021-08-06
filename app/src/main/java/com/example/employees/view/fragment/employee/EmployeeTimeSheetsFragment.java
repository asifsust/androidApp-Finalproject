package com.example.employees.view.fragment.employee;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.databinding.FragmentEmployeeTimeSheetsBinding;
import com.example.employees.network.callingApi.TimeSheetApi;
import com.example.employees.others.GlobalMethods;

public class EmployeeTimeSheetsFragment extends Fragment {

    private FragmentEmployeeTimeSheetsBinding binding;
    private Context context;
    private GlobalMethods globalMethods;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeeTimeSheetsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        getData();
        binding.refreshLayout.setOnRefreshListener(this::getData);
    }

    private void getData() {
        binding.refreshLayout.setRefreshing(false);
        TimeSheetApi api = new TimeSheetApi(context,binding.recyclerViewEmployeeSheet, globalMethods.getEmpUserId());
        api.timeSheet();
    }

    private void init() {
        context = requireContext();
        globalMethods = new GlobalMethods(context);
        binding.recyclerViewEmployeeSheet.setHasFixedSize(true);
        binding.recyclerViewEmployeeSheet.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}