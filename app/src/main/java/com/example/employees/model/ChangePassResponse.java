
package com.example.employees.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassResponse implements Serializable
{

    @SerializedName("success")
    @Expose
    private String success;

    public String getMessage() {
        return success;
    }

    public void setMessage(String message) {
        this.success = message;
    }

}
