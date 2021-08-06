
package com.example.employees.model.timeSheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSheetResponse implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("timesheets")
    @Expose
    private ArrayList<Timesheet> timesheets = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Timesheet> getTimesheets() {
        return timesheets;
    }

    public void setTimesheets(ArrayList<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }

}
