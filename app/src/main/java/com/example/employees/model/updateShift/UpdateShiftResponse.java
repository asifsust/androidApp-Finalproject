
package com.example.employees.model.updateShift;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateShiftResponse implements Serializable
{

    @SerializedName("shift")
    @Expose
    private Shift shift;
    @SerializedName("message")
    @Expose
    private String message;

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
