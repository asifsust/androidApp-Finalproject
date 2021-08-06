package com.example.employees.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.R;
import com.example.employees.databinding.AddShiftDialogBinding;
import com.example.employees.databinding.RowOnlyShiftsBinding;
import com.example.employees.model.GetShiftsResponse;
import com.example.employees.my_interface.SimpleCallBack;
import com.example.employees.network.callingApi.DeleteShiftApi;
import com.example.employees.network.callingApi.UpdateShiftApi;
import com.example.employees.others.GlobalMethods;

import java.util.ArrayList;
import java.util.Calendar;

public class AllShiftsAdapter extends RecyclerView.Adapter<AllShiftsAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<GetShiftsResponse> list;
    private final SimpleCallBack simpleCallBack;

    private String startTime,endTime;

    public AllShiftsAdapter(Context context, ArrayList<GetShiftsResponse> list,SimpleCallBack simpleCallBack) {
        this.context = context;
        this.list = list;
        this.simpleCallBack = simpleCallBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowOnlyShiftsBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GetShiftsResponse model = list.get(position);
        holder.binding.textName.setText(model.getName());
        String time = GlobalMethods.changeTimeFormat(model.getStartTime()) +"-"+GlobalMethods.changeTimeFormat(model.getEndTime());
        //String time = model.getStartTime() +"-"+model.getEndTime();

        holder.binding.textTime.setText(time);
        holder.binding.textSerial.setText(String.valueOf(model.getSerialId()));

        holder.binding.textDeleteShift.setOnClickListener(v -> deleteWarningDialog(model.getId()));
        holder.binding.textEditShift.setOnClickListener(v -> showAddShiftDialog(model));
    }

    private void showAddShiftDialog(GetShiftsResponse model) {
        Dialog dialog = new Dialog(context);
        AddShiftDialogBinding shiftBinding = AddShiftDialogBinding.inflate(LayoutInflater.from(context),null,false);
        dialog.setContentView(shiftBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        shiftBinding.buttonStartTime.setOnClickListener(v -> selectTime(shiftBinding.buttonStartTime));
        shiftBinding.buttonEndTime.setOnClickListener(v -> selectTime(shiftBinding.buttonEndTime));
        shiftBinding.buttonAdd.setText(R.string.update);

        shiftBinding.buttonAdd.setOnClickListener(v -> {
            String shiftName = shiftBinding.edtShiftName.getText().toString();
            if (GlobalMethods.emptyToast(shiftName,"Enter shift name",v.getContext())
                    && GlobalMethods.emptyToast(startTime,"Choose start time",v.getContext())
                    && GlobalMethods.emptyToast(endTime,"Choose end time",v.getContext())
            ){
                dialog.dismiss();
                UpdateShiftApi api = new UpdateShiftApi(context, String.valueOf(model.getId()), shiftName, startTime, endTime, simpleCallBack);
                api.updateShift();
            }
        });

        shiftBinding.edtShiftName.setText(model.getName());
        shiftBinding.buttonStartTime.setText(GlobalMethods.changeTimeFormat(model.getStartTime()));
        shiftBinding.buttonEndTime.setText(GlobalMethods.changeTimeFormat(model.getEndTime()));

        startTime = model.getStartTime();
        endTime = model.getEndTime();

        dialog.show();
    }

    private void selectTime(AppCompatButton button) {
        Calendar calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR);
        int mMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, R.style.DialogTheme, (view, hourOfDay, minute) -> {
            String amPm;
            int myHour = hourOfDay;
            if (hourOfDay>=12){
                if (hourOfDay != 12) hourOfDay = hourOfDay - 12;
                amPm = "PM";
            }else {
                if (hourOfDay == 0) hourOfDay=12;
                amPm = "AM";
            }

            String time = hourOfDay+":"+minute+" "+amPm;
            String timeToUpload = myHour+":"+minute+":00";
            button.setText(time);

            if (button.getId() == R.id.buttonStartTime) startTime = timeToUpload;
            else endTime = timeToUpload;

        }, mHour, mMinute,true);

        timePickerDialog.show();
    }

    private void deleteWarningDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert!!");
        builder.setMessage("Are you sure want to delete the shift?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            DeleteShiftApi api = new DeleteShiftApi(context, String.valueOf(id), simpleCallBack);
            api.deleteShift();
        });
        builder.setNegativeButton("No", null);
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowOnlyShiftsBinding binding;
        public MyViewHolder(RowOnlyShiftsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
