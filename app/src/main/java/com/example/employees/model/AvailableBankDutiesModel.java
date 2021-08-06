package com.example.employees.model;

import java.util.ArrayList;

public class AvailableBankDutiesModel {
    public String week;
    public String date;
    public ArrayList<GetShiftsResponse> shiftList;

    public AvailableBankDutiesModel() {}

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<GetShiftsResponse> getShiftList() {
        return shiftList;
    }

    public void setShiftList(ArrayList<GetShiftsResponse> shiftList) {
        this.shiftList = shiftList;
    }
}
