package com.example.employees.my_interface;

import com.example.employees.model.todayAssignShifts.TodayAssignShiftsResponse;

public interface TodayShiftCallBack {
    public void todayShiftListener(TodayAssignShiftsResponse todayShiftModel);
}
