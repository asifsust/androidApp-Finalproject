
package com.example.employees.model.addShift;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddShiftResponse implements Serializable
{
    /**
     *
     */
    @SerializedName("shift")
    @Expose
    private Shift shift;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     *
     * @return
     */
    public Shift getShift() {
        return shift;
    }

    /**
     *
     * @param shift
     */
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
