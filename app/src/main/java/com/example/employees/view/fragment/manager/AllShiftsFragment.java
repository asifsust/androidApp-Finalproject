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

import com.example.employees.adapter.AllShiftsAdapter;
import com.example.employees.databinding.FragmentAllShiftsBinding;
import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.GetWardsResponse;
import com.example.employees.my_interface.CallBack;
import com.example.employees.network.callingApi.GetShiftsApi;
import com.example.employees.others.CustomLoadingDialog;
import com.example.employees.others.GlobalMethods;

import java.util.ArrayList;

public class AllShiftsFragment extends Fragment {

    private final String TAG = this.getClass().getName();
    private FragmentAllShiftsBinding binding;
    private Context context;
    private AllShiftsAdapter adapter;
    private CustomLoadingDialog loadingDialog;

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
        binding = FragmentAllShiftsBinding.inflate(inflater,container,false);
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
        init();
        getData();
        binding.refreshLayout.setOnRefreshListener(this::getData);
    }

    /**
     *
     */
    private void init() {
        context = requireContext();
        GlobalMethods.showAddMenu(false);
        GlobalMethods.showAddShift(true);
        loadingDialog = new CustomLoadingDialog(context);
        binding.shiftsRecyclerView.setHasFixedSize(true);
        binding.shiftsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    /**
     *
     */
    private void getData() {
        loadingDialog.start();
        binding.refreshLayout.setRefreshing(false);
        GetShiftsApi api = new GetShiftsApi(context, new CallBack() {
            @Override
            public void getShiftCallBack(ArrayList<GetShiftsResponse> shiftList) {
                loadingDialog.dismiss();
                if (shiftList.size() != 0){

                    for (int i=0;i<shiftList.size();i++){
                        GetShiftsResponse model = shiftList.get(i);
                        model.setSerialId(i+1);
                        shiftList.set(i,model);
                    }

                    adapter = new AllShiftsAdapter(context, shiftList, () -> getData());
                    binding.shiftsRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else Toast.makeText(context, "No available shifts found", Toast.LENGTH_SHORT).show();
            }

            /**
             *
             * @param wardList
             */
            @Override
            public void getWardCallBack(ArrayList<GetWardsResponse> wardList) {

            }
        });
        api.getShift();
    }
}