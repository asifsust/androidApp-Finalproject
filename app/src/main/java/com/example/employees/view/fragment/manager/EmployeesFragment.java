package com.example.employees.view.fragment.manager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.adapter.EmployeesAdapter;
import com.example.employees.databinding.FragmentEmployeesBinding;
import com.example.employees.model.employees.EmployeeData;
import com.example.employees.network.callingApi.EmployeesApi;
import com.example.employees.others.GlobalMethods;

import java.util.ArrayList;

public class EmployeesFragment extends Fragment {

    private FragmentEmployeesBinding binding;
    private ArrayList<EmployeeData> employeeList;
    private EmployeesAdapter employeesAdapter;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GlobalMethods.showAddMenu(true);
        GlobalMethods.showAddShift(false);
        GlobalMethods.showAddWard(false);
        navController = Navigation.findNavController(binding.getRoot());
        setRecyclerView();
        getEmployees();
        binding.refreshLayout.setOnRefreshListener(this::getEmployees);

    }

    private void setRecyclerView() {
        employeeList = new ArrayList<>();
        RecyclerView recyclerView = binding.rvEmployees;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        employeesAdapter = new EmployeesAdapter(requireContext(), employeeList, navController, this::getEmployees);
        recyclerView.setAdapter(employeesAdapter);
    }

    private void getEmployees() {
        binding.refreshLayout.setRefreshing(false);
        employeeList.clear();
        EmployeesApi employeesApi = new EmployeesApi(requireContext(), employeeList, employeesAdapter);
        employeesApi.getEmployees();
    }
}