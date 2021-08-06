package com.example.employees.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.databinding.RowAllShiftsBinding;
import com.example.employees.model.allAssignShifts.Datum;
import com.example.employees.others.GlobalMethods;

import java.util.ArrayList;

public class AllAssignShiftsAdapter extends RecyclerView.Adapter<AllAssignShiftsAdapter.AssignShiftViewHolder> {

    private final Context context;
    private final ArrayList<Datum> allShiftList;
    private final NavController navController;

    public AllAssignShiftsAdapter(Context context, ArrayList<Datum> allShiftList, NavController navController) {
        this.context = context;
        this.allShiftList = allShiftList;
        this.navController = navController;
    }

    @NonNull
    @Override
    public AssignShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignShiftViewHolder(RowAllShiftsBinding.inflate(LayoutInflater.from(parent.getContext()),null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssignShiftViewHolder holder, int position) {
        Datum model = allShiftList.get(position);
        holder.rowAllShiftsBinding.textDate.setText(GlobalMethods.changeDateFormat(model.getDate()));

        holder.rowAllShiftsBinding.recyclerViewAssignShifts.setHasFixedSize(true);
        holder.rowAllShiftsBinding.recyclerViewAssignShifts.setLayoutManager(new LinearLayoutManager(context));
        AssignShiftsAdapter adapter = new AssignShiftsAdapter(context, model.getAssignShiftInfo(), navController);
        holder.rowAllShiftsBinding.recyclerViewAssignShifts.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return allShiftList.size();
    }

    public static class AssignShiftViewHolder extends RecyclerView.ViewHolder {
        RowAllShiftsBinding rowAllShiftsBinding;
        public AssignShiftViewHolder(RowAllShiftsBinding rowAllShiftsBinding) {
            super(rowAllShiftsBinding.getRoot());
            this.rowAllShiftsBinding = rowAllShiftsBinding;
        }
    }
}
