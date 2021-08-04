package com.example.employees.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.R;
import com.example.employees.databinding.ConfirmRemoveEmpDialogBinding;
import com.example.employees.databinding.RemoveEmployeDialogBinding;
import com.example.employees.databinding.RowEmployessBinding;
import com.example.employees.model.employees.EmployeeData;
import com.example.employees.network.callingApi.RemoveEmployeeApi;
import com.example.employees.session.UserSession;

import java.util.ArrayList;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<EmployeeData> list;
    private NavController navController;
    private UserSession userSession;

    public EmployeesAdapter(Context context, ArrayList<EmployeeData> list, NavController navController) {
        this.context = context;
        this.list = list;
        this.navController = navController;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowEmployessBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EmployeeData data = list.get(position);

        holder.binding.textName.setText(data.getName());
        holder.binding.textRole.setText(data.getRole());
        holder.binding.textUserId.setText(String.valueOf(data.getUserId()));
        holder.binding.textSerial.setText(String.valueOf(data.getEmployeeId()));

        holder.binding.textViewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "view", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object",data);
                navController.navigate(R.id.action_employeesFragment_to_viewEmployeeFragment,bundle);
            }
        });

        holder.binding.textEditEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object",data);
                navController.navigate(R.id.action_employeesFragment_to_addNewEmployeeFragmentForUpdate,bundle);
            }
        });

        holder.binding.textRemoveEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show();
                showEmpRemoveDialog(data);
            }
        });
    }

    private void showEmpRemoveDialog(EmployeeData data) {
        RemoveEmployeDialogBinding binding = RemoveEmployeDialogBinding.inflate(LayoutInflater.from(context),null,false);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.imageClose.setOnClickListener(v -> dialog.dismiss());
        binding.buttonCancel.setOnClickListener(v -> dialog.dismiss());

        binding.textName.setText(data.getName());
        binding.textUserId.setText(String.valueOf(data.getUserId()));
        binding.textRole.setText(data.getRole());

        binding.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmRemoveDialog(data.getId(),dialog);
            }
        });

        dialog.show();
    }

    private void showConfirmRemoveDialog(int id,Dialog dialogRoot) {
        userSession = new UserSession(context);
        ConfirmRemoveEmpDialogBinding binding = ConfirmRemoveEmpDialogBinding.inflate(LayoutInflater.from(context),null,false);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.imageClose.setOnClickListener(v -> dialog.dismiss());
        binding.buttonCancel.setOnClickListener(v -> dialog.dismiss());

        binding.buttonConfirm.setOnClickListener(v -> {

            String password = binding.edtPassword.getText().toString();
            if (password.equals(userSession.getAdminPass())){
                RemoveEmployeeApi removeEmployeeApi = new RemoveEmployeeApi(context,String.valueOf(id));
                removeEmployeeApi.removeEmployee();
                dialog.dismiss();
                dialogRoot.dismiss();
            }
            else {
                dialog.dismiss();
                Toast.makeText(context, "Password not matched", Toast.LENGTH_SHORT).show();
            }

        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowEmployessBinding binding;
        public MyViewHolder(RowEmployessBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
