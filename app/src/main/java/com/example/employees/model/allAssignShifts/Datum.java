
package com.example.employees.model.allAssignShifts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("assign_shift_info")
    @Expose
    private ArrayList<AssignShiftInfo> assignShiftInfo = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<AssignShiftInfo> getAssignShiftInfo() {
        return assignShiftInfo;
    }

    public void setAssignShiftInfo(ArrayList<AssignShiftInfo> assignShiftInfo) {
        this.assignShiftInfo = assignShiftInfo;
    }

}
