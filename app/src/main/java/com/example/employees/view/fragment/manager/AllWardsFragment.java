package com.example.employees.view.fragment.manager;

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

import com.example.employees.adapter.AllWardsAdapter;
import com.example.employees.databinding.FragmentAllWardsBinding;
import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.GetWardsResponse;
import com.example.employees.my_interface.CallBack;
import com.example.employees.network.callingApi.GetWardApi;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.others.GlobalMethods;

import java.util.ArrayList;

public class AllWardsFragment extends Fragment {

    private FragmentAllWardsBinding binding;
    private Context context;
    private AllWardsAdapter adapter;
    private CustomLoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllWardsBinding.inflate(inflater,container,false);
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
        loadingDialog.start();
        GetWardApi api = new GetWardApi(context, new CallBack() {
            @Override
            public void getShiftCallBack(ArrayList<GetShiftsResponse> shiftList) {

            }

            @Override
            public void getWardCallBack(ArrayList<GetWardsResponse> wardList) {
                loadingDialog.dismiss();
                if (wardList.size() != 0){
                    for (int i=0;i<wardList.size();i++){
                        GetWardsResponse model = wardList.get(i);
                        model.setSerialId(i+1);
                        wardList.set(i,model);
                    }
                    adapter = new AllWardsAdapter(context, wardList, () -> getData());
                    binding.wardRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else Toast.makeText(context, "No available wards", Toast.LENGTH_SHORT).show();
            }
        });
        api.getWard();
    }

    private void init() {
        GlobalMethods.showAddMenu(false);
        GlobalMethods.showAddWard(true);
        context = requireContext();
        loadingDialog = new CustomLoadingDialog(context);
        binding.wardRecyclerView.setHasFixedSize(true);
        binding.wardRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }
}