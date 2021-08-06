package com.example.employees.view.fragment.manager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employees.databinding.FragmentTimesheetsBinding;
import com.example.employees.network.callingApi.TimeSheetApi;
import com.example.employees.others.GlobalMethods;

public class TimesheetsFragment extends Fragment {

    private FragmentTimesheetsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTimesheetsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GlobalMethods.showAddMenu(false);

        initRecyclerView();
        getData();

        binding.refreshLayout.setOnRefreshListener(this::getData);
    }

    private void getData() {
        binding.refreshLayout.setRefreshing(false);
        TimeSheetApi api = new TimeSheetApi(requireContext(),binding.recyclerViewTimeSheet,"");
        api.timeSheet();
    }

    private void initRecyclerView() {
        binding.recyclerViewTimeSheet.setHasFixedSize(true);
        binding.recyclerViewTimeSheet.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}