package com.example.employees.view.fragment.manager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.databinding.FragmentShiftsBinding;
import com.example.employees.network.callingApi.GetAllShiftsApi;
import com.example.employees.others.GlobalMethods;
import com.example.employees.others.GlobalValues;

public class ShiftsFragment extends Fragment {

    public static FragmentShiftsBinding binding;
    @SuppressLint("StaticFieldLeak")
    public static NavController navController;

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

        binding = FragmentShiftsBinding.inflate(inflater,container,false);
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

        navController = Navigation.findNavController(binding.getRoot());
        initRecycler();
        getShifts();
        GlobalMethods.showAddMenu(false);

        binding.refreshLayout.setOnRefreshListener(this::getShifts);

    }

    /**
     *
     */
    private void initRecycler() {
        binding.recyclerViewShift.setHasFixedSize(true);
        binding.recyclerViewShift.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    /**
     *
     */
    public void getShifts(){
        GlobalValues.isManager = true;
        binding.refreshLayout.setRefreshing(false);
        GetAllShiftsApi getAllShiftsApi = new GetAllShiftsApi(requireContext(),binding.recyclerViewShift,navController,"");
        getAllShiftsApi.allShift();
    }
}