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
import android.widget.Toast;

import com.example.employees.adapter.AvailableBankDutiesAdapter;
import com.example.employees.databinding.FragmentAvailableBankDutiesBinding;
import com.example.employees.model.AvailableBankDutiesModel;
import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.GetWardsResponse;
import com.example.employees.my_interface.CallBack;
import com.example.employees.network.callingApi.GetShiftsApi;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.others.GlobalMethods;

import java.util.ArrayList;


public class AvailableBankDutiesFragment extends Fragment {

    private FragmentAvailableBankDutiesBinding binding;
    private Context context;
    private ArrayList<AvailableBankDutiesModel> bankDutiesList;
    private AvailableBankDutiesAdapter adapter;
    private CustomLoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAvailableBankDutiesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        getData();
    }

    private void getData() {
        loadingDialog.start();
        GetShiftsApi api = new GetShiftsApi(context, new CallBack() {
            @Override
            public void getShiftCallBack(ArrayList<GetShiftsResponse> shiftList) {
                if (shiftList.size() != 0){
                    for (int i=0;i<5;i++){
                        AvailableBankDutiesModel model = new AvailableBankDutiesModel();
                        model.setDate(GlobalMethods.getNextDate(i));
                        model.setWeek(GlobalMethods.getNextWeek(i));
                        model.setShiftList(shiftList);
                        bankDutiesList.add(model);

                        adapter = new AvailableBankDutiesAdapter(context,bankDutiesList);
                        binding.availableBankDutiesRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                }
                else Toast.makeText(context, "No shift available", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }

            @Override
            public void getWardCallBack(ArrayList<GetWardsResponse> wardList) {

            }
        });
        api.getShift();
    }

    private void init() {
        context = requireContext();
        loadingDialog = new CustomLoadingDialog(context);
        binding.availableBankDutiesRecyclerView.setHasFixedSize(true);
        binding.availableBankDutiesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        bankDutiesList = new ArrayList<>();
    }
}