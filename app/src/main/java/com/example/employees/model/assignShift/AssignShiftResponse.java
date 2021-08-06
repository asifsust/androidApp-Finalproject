
package com.example.employees.model.assignShift;

import java.io.Serializable;

import com.example.employees.model.GetShiftsResponse;
import com.example.employees.model.GetWardsResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignShiftResponse implements Serializable
{

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("shift")
    @Expose
    private GetShiftsResponse shift;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("ward")
    @Expose
    private GetWardsResponse ward;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("message")
    @Expose
    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GetShiftsResponse getShift() {
        return shift;
    }

    public void setShift(GetShiftsResponse shift) {
        this.shift = shift;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GetWardsResponse getWard() {
        return ward;
    }

    public void setWard(GetWardsResponse ward) {
        this.ward = ward;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
