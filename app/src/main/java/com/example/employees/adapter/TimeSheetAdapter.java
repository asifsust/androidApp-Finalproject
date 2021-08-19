package com.example.employees.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.databinding.RowTimesheetsBinding;
import com.example.employees.databinding.ViewShiftDialogBinding;
import com.example.employees.model.timeSheet.Timesheet;
import com.example.employees.others.GlobalMethods;

import java.util.ArrayList;

public class TimeSheetAdapter extends RecyclerView.Adapter<TimeSheetAdapter.TimeSheetViewHolder> {

    private final Context context;
    private final ArrayList<Timesheet> timeSheetList;

    public TimeSheetAdapter(Context context, ArrayList<Timesheet> timeSheetList) {
        this.context = context;
        this.timeSheetList = timeSheetList;
    }

    @NonNull
    @Override
    public TimeSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeSheetViewHolder(RowTimesheetsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSheetViewHolder holder, int position) {
        Timesheet model = timeSheetList.get(position);
        holder.binding.textDate.setText(GlobalMethods.changeDateFormat(model.getDate()));

        String time = GlobalMethods.changeTimeFormat(model.getShift().getStartTime())+"-"+GlobalMethods.changeTimeFormat(model.getShift().getEndTime());

        holder.binding.textTime.setText(time);

        holder.binding.buttonShowDetails.setOnClickListener(v -> showDetailsDialog(model));
    }

    private void showDetailsDialog(Timesheet model) {
        ViewShiftDialogBinding binding;
        binding = ViewShiftDialogBinding.inflate(LayoutInflater.from(context),null,false);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String time = GlobalMethods.changeTimeFormat(model.getShift().getStartTime())+"-"+GlobalMethods.changeTimeFormat(model.getShift().getEndTime());

        binding.textDate.setText(GlobalMethods.changeDateFormat(model.getDate()));
        binding.textTime.setText(time);
        binding.textUserId.setText(String.valueOf(model.getUser().getUserId()));
        binding.textRole.setText(model.getUser().getRole());
        binding.textBookedTo.setText(model.getUser().getName());
        binding.textWardName.setText(model.getWard().getName());

        binding.imageClose.setOnClickListener(v -> dialog.dismiss());
        binding.buttonCancelShift.setVisibility(View.GONE);

        dialog.show();
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return timeSheetList.size();
    }

    /**
     *
     */
    public static class TimeSheetViewHolder extends RecyclerView.ViewHolder {
        private final RowTimesheetsBinding binding;
        public TimeSheetViewHolder(RowTimesheetsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
