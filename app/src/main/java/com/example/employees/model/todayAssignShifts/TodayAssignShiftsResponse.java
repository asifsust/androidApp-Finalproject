
package com.example.employees.model.todayAssignShifts;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayAssignShiftsResponse implements Serializable
{

    @SerializedName("today_assign_shifts")
    @Expose
    private List<TodayAssignShift> todayAssignShifts = null;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     *
     * @return
     */
    public List<TodayAssignShift> getTodayAssignShifts() {
        return todayAssignShifts;
    }

    /**
     *
     * @param todayAssignShifts
     */
    public void setTodayAssignShifts(List<TodayAssignShift> todayAssignShifts) {
        this.todayAssignShifts = todayAssignShifts;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
