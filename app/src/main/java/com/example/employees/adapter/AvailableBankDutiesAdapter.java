package com.example.employees.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.databinding.RowAvailableBankDutiesBinding;
import com.example.employees.model.AvailableBankDutiesModel;

import java.util.ArrayList;

public class AvailableBankDutiesAdapter extends RecyclerView.Adapter<AvailableBankDutiesAdapter.MyViewHolder> {
    /**
     *
     */
    private final Context context;
    private final ArrayList<AvailableBankDutiesModel> myList;

    public AvailableBankDutiesAdapter(Context context, ArrayList<AvailableBankDutiesModel> myList) {
        this.context = context;
        this.myList = myList;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowAvailableBankDutiesBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AvailableBankDutiesModel model = myList.get(position);
        holder.binding.textDate.setText(model.getDate());
        holder.binding.textWeek.setText(model.getWeek());

        holder.binding.recyclerViewAvailableDuties.setHasFixedSize(true);
        holder.binding.recyclerViewAvailableDuties.setLayoutManager(new LinearLayoutManager(context));
        AvailableDutiesAdapter availableDutiesAdapter = new AvailableDutiesAdapter(context, model.getShiftList(),model.getDate());
        holder.binding.recyclerViewAvailableDuties.setAdapter(availableDutiesAdapter);
        availableDutiesAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    /**
     *
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowAvailableBankDutiesBinding binding;
        public MyViewHolder(RowAvailableBankDutiesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
