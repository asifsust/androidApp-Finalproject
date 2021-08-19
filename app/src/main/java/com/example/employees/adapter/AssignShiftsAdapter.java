package com.example.employees.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.databinding.RowShiftsBinding;
import com.example.employees.databinding.ViewShiftDialogBinding;
import com.example.employees.model.allAssignShifts.AssignShiftInfo;
import com.example.employees.network.callingApi.CancelShiftApi;
import com.example.employees.others.GlobalMethods;
import com.example.employees.others.GlobalValues;

import java.util.ArrayList;

public class AssignShiftsAdapter extends RecyclerView.Adapter<AssignShiftsAdapter.AssignShiftsViewHolder> {

    private final Context context;
    private final ArrayList<AssignShiftInfo> assignList;
    private final NavController navController;

    /**
     *
     * @param context
     * @param assignList
     * @param navController
     */
    public AssignShiftsAdapter(Context context, ArrayList<AssignShiftInfo> assignList,NavController navController) {
        this.context = context;
        this.assignList = assignList;
        this.navController = navController;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public AssignShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignShiftsViewHolder(RowShiftsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AssignShiftsViewHolder holder, int position) {
        AssignShiftInfo model = assignList.get(position);
        holder.binding.textDate.setText(GlobalMethods.changeDateFormat(model.getDate()));

        String time = GlobalMethods.changeTimeFormat(model.getShift().getStartTime())+"-"+GlobalMethods.changeTimeFormat(model.getShift().getEndTime());
        holder.binding.textTime.setText(time);

        holder.itemView.setOnClickListener(v -> showViewShiftDialog(model));
    }

    /**
     *
     * @param model
     */
    private void showViewShiftDialog(AssignShiftInfo model) {
        ViewShiftDialogBinding binding;
        binding = ViewShiftDialogBinding.inflate(LayoutInflater.from(context),null,false);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String time = GlobalMethods.changeTimeFormat(model.getShift().getStartTime())+"-"+GlobalMethods.changeTimeFormat(model.getShift().getEndTime());
        /**
         *
         */
        binding.textDate.setText(GlobalMethods.changeDateFormat(model.getDate()));
        binding.textTime.setText(time);
        binding.textUserId.setText(String.valueOf(model.getUser().getUserId()));
        binding.textRole.setText(model.getUser().getRole());
        binding.textBookedTo.setText(model.getUser().getName());
        binding.textWardName.setText(model.getWard().getName());
        /**
         *
         */
        if (!GlobalValues.isManager) binding.buttonCancelShift.setVisibility(View.GONE);

        binding.imageClose.setOnClickListener(v -> dialog.dismiss());
        binding.buttonCancelShift.setOnClickListener(v -> {
            dialog.dismiss();
            CancelShiftApi cancelShiftApi = new CancelShiftApi(context,String.valueOf(model.getId()),navController);
            cancelShiftApi.cancelShift();
        });

        dialog.show();
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return assignList.size();
    }

    public static class AssignShiftsViewHolder extends RecyclerView.ViewHolder {
        RowShiftsBinding binding;
        public AssignShiftsViewHolder(RowShiftsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
