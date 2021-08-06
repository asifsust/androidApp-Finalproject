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

import com.example.employees.databinding.FragmentPersonalRoasterBinding;
import com.example.employees.network.callingApi.GetAllShiftsApi;
import com.example.employees.others.GlobalMethods;
import com.example.employees.others.GlobalValues;

public class PersonalRoasterFragment extends Fragment {

    private FragmentPersonalRoasterBinding binding;
    private Context context;
    private GlobalMethods globalMethods;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPersonalRoasterBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        GlobalValues.isManager = false;
        getData();
        binding.refreshLayout.setOnRefreshListener(this::getData);

    }

    private void getData() {
        binding.refreshLayout.setRefreshing(false);
        GetAllShiftsApi api = new GetAllShiftsApi(context,binding.recyclerViewPersonalRoaster,null, globalMethods.getEmpUserId());
        api.allShift();
    }

    private void init() {
        context = requireContext();
        globalMethods = new GlobalMethods(context);
        binding.recyclerViewPersonalRoaster.setHasFixedSize(true);
        binding.recyclerViewPersonalRoaster.setLayoutManager(new LinearLayoutManager(context));
    }
}