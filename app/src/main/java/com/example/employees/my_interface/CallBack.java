package com.example.employees.my_interface;

import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.GetWardsResponse;

import java.util.ArrayList;

public interface CallBack {
    void getShiftCallBack(ArrayList<GetShiftsResponse> shiftList);
    void getWardCallBack(ArrayList<GetWardsResponse> wardList);
}
