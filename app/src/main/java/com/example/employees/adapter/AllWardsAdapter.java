package com.example.employees.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employees.R;
import com.example.employees.databinding.AddWardDialogBinding;
import com.example.employees.databinding.RowAllWardsBinding;
import com.example.employees.model.GetWardsResponse;
import com.example.employees.my_interface.SimpleCallBack;
import com.example.employees.network.callingApi.DeleteWardApi;
import com.example.employees.network.callingApi.UpdateWardApi;
import com.example.employees.others.GlobalMethods;

import java.util.ArrayList;

public class AllWardsAdapter extends RecyclerView.Adapter<AllWardsAdapter.MyViewHolder> {
    /**
     *
     */
    private final Context context;
    private final ArrayList<GetWardsResponse> list;
    private final SimpleCallBack simpleCallBack;

    /**
     *
     * @param context
     * @param list
     * @param simpleCallBack
     */
    public AllWardsAdapter(Context context, ArrayList<GetWardsResponse> list,SimpleCallBack simpleCallBack) {
        this.context = context;
        this.list = list;
        this.simpleCallBack = simpleCallBack;
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
        return new MyViewHolder(RowAllWardsBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GetWardsResponse model = list.get(position);
        holder.binding.textWardName.setText(model.getName());
        holder.binding.textSerial.setText(String.valueOf(model.getSerialId()));

        holder.binding.textDeleteWard.setOnClickListener(v -> deleteWarningDialog(model.getId()));
        holder.binding.textEditWard.setOnClickListener(v -> showUpdateWardDialog(model));
    }

    /**
     *
     * @param model
     */
    private void showUpdateWardDialog(GetWardsResponse model) {
        Dialog dialog = new Dialog(context);
        AddWardDialogBinding wardDialogBinding = AddWardDialogBinding.inflate(LayoutInflater.from(context),null,false);
        dialog.setContentView(wardDialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        wardDialogBinding.buttonAdd.setOnClickListener(v -> {
            String wardName = wardDialogBinding.edtWardName.getText().toString();
            if (GlobalMethods.emptyToast(wardName,"Enter ward name",v.getContext())){
                dialog.dismiss();
                UpdateWardApi api = new UpdateWardApi(context,String.valueOf(model.getId()),wardName,simpleCallBack);
                api.updateWard();
            }
        });
        /**
         *
         */
        wardDialogBinding.buttonAdd.setText(R.string.update);
        wardDialogBinding.edtWardName.setText(model.getName());

        dialog.show();
    }

    /**
     *
     * @param id
     */
    private void deleteWarningDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert!!");
        builder.setMessage("Are you sure want to delete the ward?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            DeleteWardApi api = new DeleteWardApi(context, String.valueOf(id), simpleCallBack);
            api.deleteWard();
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
        private final RowAllWardsBinding binding;
        public MyViewHolder(RowAllWardsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
