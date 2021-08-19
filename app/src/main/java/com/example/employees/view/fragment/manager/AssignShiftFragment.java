package com.example.employees.view.fragment.manager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.employees.R;
import com.example.employees.databinding.FragmentAddNewShiftBinding;
import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.GetWardsResponse;
import com.example.employees.my_interface.CallBack;
import com.example.employees.network.callingApi.AssignShiftApi;
import com.example.employees.network.callingApi.GetAllShiftsApi;
import com.example.employees.network.callingApi.GetShiftsApi;
import com.example.employees.network.callingApi.GetWardApi;
import com.example.employees.others.CustomDatePickerDialog;
import com.example.employees.others.GlobalMethods;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class AssignShiftFragment extends Fragment{

    private FragmentAddNewShiftBinding binding;
    private ArrayList<GetShiftsResponse> myShiftsList;
    private ArrayList<GetWardsResponse> myWardsList;

    private String user_id;

    private String selectedShiftId;
    private String selectedWardId;
    private String selectedDate;

    private ArrayAdapter<String> shiftNameAdapter;
    private ArrayAdapter<String> wardNameAdapter;
    private ArrayList<String> shiftNameList;
    private ArrayList<String> wardNameList;

    private NavController navController;
    private boolean isBooked = true;
    private Context context;
    private String shiftEndTime,shiftStartTime;
    private final String TAG = this.getClass().getName();

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

        binding = FragmentAddNewShiftBinding.inflate(inflater,container,false);
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

        Calendar calendar = Calendar.getInstance();
        int minYear = calendar.get(Calendar.YEAR);
        int minMonth = calendar.get(Calendar.MONTH);
        int minDay = calendar.get(Calendar.DAY_OF_MONTH);
        int maxYear = 2050;
        int maxMonth = 12;
        int maxDay = 31;

        binding.buttonSelectDate.setOnClickListener(v -> {

            DatePickerDialog myDatePicker = new CustomDatePickerDialog(requireContext(), (view12, year, month, dayOfMonth) -> {
                month++;
                binding.buttonSelectDate.setText(
                        new StringBuilder().append(year).append("-")
                                .append(month).append("-").append(dayOfMonth)
                );

                if (month<10 && dayOfMonth<10) selectedDate = year+"-0"+month+"-0"+dayOfMonth;
                if (dayOfMonth>9 && month<10) selectedDate = year+"-0"+month+"-"+dayOfMonth;
                if (dayOfMonth<10 && month>9) selectedDate = year+"-"+month+"-0"+dayOfMonth;
                if (dayOfMonth>9 && month>9)  selectedDate = year+"-"+month+"-"+dayOfMonth;

                checkTodayBook();
                Log.d(TAG, "onViewCreated: date: "+selectedDate);
            },minYear,minMonth,minDay,maxYear,maxMonth,maxDay);

            myDatePicker.show();

        });
        /**
         *
         */
        binding.buttonConfirmShift.setOnClickListener(v -> {
            String shift_id = selectedShiftId;
            String ward_id = selectedWardId;
            String date = selectedDate;

            if (GlobalMethods.emptyToast(shift_id,getString(R.string.select_shift),requireContext())
                && GlobalMethods.emptyToast(ward_id,getString(R.string.select_ward),requireContext())
                && GlobalMethods.emptyToast(date,getString(R.string.select_date),requireContext())
                && validDate()
            ){
                AssignShiftApi assignShiftApi = new AssignShiftApi(requireContext(),user_id,shift_id,ward_id,date,navController);
                assignShiftApi.assignShift();
            }

        });

        getShifts();
        getWards();
        /**
         *
         */
        binding.spinnerShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    selectedShiftId = String.valueOf(myShiftsList.get(position-1).getId());
                    shiftEndTime = myShiftsList.get(position-1).getEndTime();
                    shiftStartTime = myShiftsList.get(position-1).getStartTime();

                    if (selectedDate != null && isToday()) {
                        try {
                            checkValidTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    selectedShiftId = "";
                }
            }

            /**
             *
             * @param parent
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedWardId = String.valueOf(myWardsList.get(position-1).getId());
                else selectedWardId = "";
            }

            /**
             *
             * @param parent
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *
     * @return
     */
    private boolean isToday() {
        return GlobalMethods.getCurrentDate().equals(selectedDate);
    }

    /**
     *
     * @throws ParseException
     */
    private void checkValidTime() throws ParseException {

        if (!GlobalMethods.isValidTimeForShift(shiftStartTime,shiftEndTime,GlobalMethods.getCurrentTime())){
            Toast.makeText(context, "Invalid shift, try with another shift or date", Toast.LENGTH_SHORT).show();
            binding.spinnerShift.setSelection(0);
            selectedShiftId = "";
        }
    }

    /**
     *
     */
    private void init() {
        context = requireContext();
        GlobalMethods.showAddMenu(false);
        user_id = requireArguments().getString("user_id");
        navController = Navigation.findNavController(binding.getRoot());

        myShiftsList = new ArrayList<>();
        shiftNameList = new ArrayList<>();
        myWardsList = new ArrayList<>();
        wardNameList = new ArrayList<>();
    }

    /**
     *
     */
    private void checkTodayBook() {
        GetAllShiftsApi api = new GetAllShiftsApi(context, selectedDate, user_id, message -> {
            if (message.equals("ok")){
                isBooked = false;
                if (isToday() && shiftEndTime!=null){
                    checkValidTime();
                }
            }
            else {
                isBooked = true;
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
        api.allShiftByDate();
    }

    /**
     *
     */
    private void getWards() {
        GetWardApi getWardApi = new GetWardApi(requireContext(), new CallBack() {
            @Override
            public void getShiftCallBack(ArrayList<GetShiftsResponse> shiftList) {

            }

            /**
             *
             * @param wardList
             */
            @Override
            public void getWardCallBack(ArrayList<GetWardsResponse> wardList) {
                wardNameList.add("Select Ward");
                myWardsList.addAll(wardList);
                for (GetWardsResponse data:wardList){
                    wardNameList.add(data.getName());
                }

                wardNameAdapter = new ArrayAdapter<>(
                        requireContext(),
                        R.layout.spinner_layout,
                        R.id.textSpinner,
                        wardNameList
                );

                binding.spinnerWard.setAdapter(wardNameAdapter);
            }
        });

        getWardApi.getWard();
    }

    private void getShifts() {

        GetShiftsApi getShiftsApi = new GetShiftsApi(requireContext(), new CallBack() {
            @Override
            public void getShiftCallBack(ArrayList<GetShiftsResponse> shiftList) {
                shiftNameList.add("Select Shift");
                myShiftsList.addAll(shiftList);
                for (GetShiftsResponse data:shiftList){
                    shiftNameList.add(data.getName());
                }

                shiftNameAdapter = new ArrayAdapter<>(
                        requireContext(),
                        R.layout.spinner_layout,
                        R.id.textSpinner,
                        shiftNameList
                );

                binding.spinnerShift.setAdapter(shiftNameAdapter);
            }

            @Override
            public void getWardCallBack(ArrayList<GetWardsResponse> wardList) {

            }
        });

        getShiftsApi.getShift();
    }

    public boolean validDate(){
        if (isBooked){
            Toast.makeText(context, "Please choose another date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}