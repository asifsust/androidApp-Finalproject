package com.example.employees.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.R;
import com.example.employees.databinding.ConfirmBookingDialogBinding;
import com.example.employees.databinding.RowAvailableDutyBinding;
import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.GetWardsResponse;
import com.example.employees.my_interface.CallBack;
import com.example.employees.network.callingApi.AssignShiftApi;
import com.example.employees.network.callingApi.GetAllShiftsApi;
import com.example.employees.network.callingApi.GetWardApi;
import com.example.employees.others.GlobalMethods;
import com.skydoves.expandablelayout.ExpandableLayout;

import java.text.ParseException;
import java.util.ArrayList;

public class AvailableDutiesAdapter extends RecyclerView.Adapter<AvailableDutiesAdapter.MyViewHolder> {

    private final String TAG = this.getClass().getName();
    private final Context context;
    private final ArrayList<GetShiftsResponse> myList;
    private final String date;

    private ArrayList<GetWardsResponse> myWardsList;
    private String selectedWardId;
    private ArrayAdapter<String> wardNameAdapter;
    private ArrayList<String> wardNameList;

    private final GlobalMethods globalMethods;

    public AvailableDutiesAdapter(Context context, ArrayList<GetShiftsResponse> myList,String date) {
        this.context = context;
        this.myList = myList;
        this.date = date;
        globalMethods = new GlobalMethods(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowAvailableDutyBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GetShiftsResponse model = myList.get(position);

        TextView shiftNameTV,roleTV,timeTV,restTV,workTimeTV;
        ImageView imageArrowDown;
        AppCompatButton bookBankBtn;
        ExpandableLayout layout = holder.binding.expandable;
        shiftNameTV    = layout.parentLayout.findViewById(R.id.textShiftName);
        roleTV         = layout.parentLayout.findViewById(R.id.textRole);
        timeTV         = layout.parentLayout.findViewById(R.id.textTime);
        imageArrowDown = layout.parentLayout.findViewById(R.id.imageArrowDown);

        bookBankBtn    = layout.secondLayout.findViewById(R.id.buttonBookBank);
        restTV         = layout.secondLayout.findViewById(R.id.textRest);
        workTimeTV     = layout.secondLayout.findViewById(R.id.textWorkTime);

        String time = GlobalMethods.changeTimeFormat(model.getStartTime())+"-"+GlobalMethods.changeTimeFormat(model.getEndTime());

        shiftNameTV.setText(model.getName());
        roleTV.setText("RGN");
        timeTV.setText(time);
        if (model.getName().toLowerCase().equals("night")) {
            restTV.setText("1h");
            workTimeTV.setText("11h");
        }
        else {
            restTV.setText("30min");
            workTimeTV.setText("7h 30min");
        }


        bookBankBtn.setOnClickListener(v -> {
            debug(model);
            if (GlobalMethods.changeDateFormat(GlobalMethods.getCurrentDate()).equals(date)){
                try {
                    if (checkValidTime(model.getStartTime(),model.getEndTime())) showBookingDialog(model);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else showBookingDialog(model);
        });

        imageArrowDown.setOnClickListener(v -> {
            if (imageArrowDown.getTag().equals("collapsed")){
                imageArrowDown.setBackgroundResource(R.drawable.ic_arrow_up_24);
                layout.expand();
                imageArrowDown.setTag("expanded");
            }
            else {
                imageArrowDown.setBackgroundResource(R.drawable.ic_arrow_down);
                layout.collapse();
                imageArrowDown.setTag("collapsed");
            }
        });
    }

    private void debug(GetShiftsResponse model) {
        Log.d(TAG, "onBindViewHolder: server_end_time: "+model.getEndTime());
        Log.d(TAG, "onBindViewHolder: local_current_time: "+GlobalMethods.getCurrentTime());
        Log.d(TAG, "onBindViewHolder: pass_date: "+date);
        Log.d(TAG, "onBindViewHolder: current_date: "+GlobalMethods.getCurrentDate());
    }


    private void showBookingDialog(GetShiftsResponse model) {
        Dialog dialog = new Dialog(context);
        ConfirmBookingDialogBinding binding  = ConfirmBookingDialogBinding.inflate(LayoutInflater.from(context),null,false);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getShifts(binding.spinnerShift);

        String time = GlobalMethods.changeTimeFormat(model.getStartTime())+"-"+GlobalMethods.changeTimeFormat(model.getEndTime());
        binding.textTime.setText(time);
        binding.textDate.setText(date);
        binding.textShift.setText(model.getName());

        binding.spinnerShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    selectedWardId = String.valueOf(myWardsList.get(position-1).getId());
                }else selectedWardId = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.buttonCancel.setOnClickListener(v -> dialog.dismiss());

        binding.buttonBookBank.setOnClickListener(v -> {
            if (selectedWardId.equals("")){
                Toast.makeText(context, "Select ward", Toast.LENGTH_SHORT).show();
                return;
            }
            GetAllShiftsApi api = new GetAllShiftsApi(context, GlobalMethods.changeDateFormatYMD(date), globalMethods.getEmpUserId(), message -> {
                if (message.equals("ok")) {
                    dialog.dismiss();
                    AssignShiftApi assignShiftApi = new AssignShiftApi(context, globalMethods.getEmpUserId(),String.valueOf(model.getId()),selectedWardId,GlobalMethods.changeDateFormatYMD(date),null);
                    assignShiftApi.assignShift();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
            api.allShiftByDate();
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowAvailableDutyBinding binding;
        public MyViewHolder(RowAvailableDutyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void getShifts(Spinner spinner) {
        myWardsList = new ArrayList<>();
        wardNameList = new ArrayList<>();
        GetWardApi getWardApi = new GetWardApi(context, new CallBack() {
            @Override
            public void getShiftCallBack(ArrayList<GetShiftsResponse> shiftList) {

            }

            @Override
            public void getWardCallBack(ArrayList<GetWardsResponse> wardList) {
                wardNameList.add("Select Ward");
                myWardsList.addAll(wardList);
                for (GetWardsResponse data:wardList){
                    wardNameList.add(data.getName());
                }

                wardNameAdapter = new ArrayAdapter<>(
                        context,
                        R.layout.spinner_layout,
                        R.id.textSpinner,
                        wardNameList
                );

                spinner.setAdapter(wardNameAdapter);
            }
        });

        getWardApi.getWard();
    }

    private boolean checkValidTime(String shiftStartTime,String shiftEndTime) throws ParseException {
        if (!GlobalMethods.isValidTimeForShift(shiftStartTime,shiftEndTime,GlobalMethods.getCurrentTime())){
            Toast.makeText(context, "Ops! you cann't assign this shift", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
