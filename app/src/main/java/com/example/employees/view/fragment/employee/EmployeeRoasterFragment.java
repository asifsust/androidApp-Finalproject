package com.example.employees.view.fragment.employee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.LoginActivity;
import com.example.employees.R;
import com.example.employees.databinding.FragmentEmployeeRoasterBinding;
import com.example.employees.model.todayAssignShifts.Shift;
import com.example.employees.model.todayAssignShifts.User;
import com.example.employees.model.todayAssignShifts.Ward;
import com.example.employees.network.callingApi.TodayShiftApi;
import com.example.employees.others.GlobalMethods;
import com.example.employees.session.UserSession;

public class EmployeeRoasterFragment extends Fragment {

    private final String TAG = this.getClass().getName();
    private FragmentEmployeeRoasterBinding binding;
    private NavController navController;
    private UserSession userSession;

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
        userSession = new UserSession(requireContext());

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

        binding.layoutLogout.setOnClickListener(v -> {
            goToLogin();
            userSession.clearUserSession();
        });

        getTodayShiftData();

    }

    private void goToLogin() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
        ((Activity)requireContext()).finish();
    }

    private void getTodayShiftData() {
        TodayShiftApi api = new TodayShiftApi(requireContext(), todayShiftModel -> {
            if (todayShiftModel.getTodayAssignShifts().size() != 0){
                for (int i=0;i<todayShiftModel.getTodayAssignShifts().size();i++){
                    Shift shift = todayShiftModel.getTodayAssignShifts().get(i).getShift();
                    Ward ward = todayShiftModel.getTodayAssignShifts().get(i).getWard();
                    User user = todayShiftModel.getTodayAssignShifts().get(i).getUser();

                    GlobalMethods globalMethods = new GlobalMethods(requireContext());
                    if (!String.valueOf(user.getId()).equals(globalMethods.getEmpUserId())) continue;

                    binding.cardTodayShift.setVisibility(View.VISIBLE);
                    binding.cardNoShift.setVisibility(View.GONE);
                    binding.textShift.setText(shift.getName());
                    binding.textWard.setText(ward.getName());
                    binding.textRole.setText(user.getRole());
                    String time = GlobalMethods.changeTimeFormat(shift.getStartTime()) +"-"+GlobalMethods.changeTimeFormat(shift.getEndTime());
                    binding.textTime.setText(time);
                }
            }else {
                Log.d(TAG, "todayShiftListener: no shift today");
            }

        });
        api.todayShift();
    }
}