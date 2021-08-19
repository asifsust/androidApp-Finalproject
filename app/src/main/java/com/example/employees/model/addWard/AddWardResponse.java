
package com.example.employees.model.addWard;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddWardResponse implements Serializable
{
    /**
     *
     */
    @SerializedName("ward")
    @Expose
    private Ward ward;
    @SerializedName("message")
    @Expose
    private String message;

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
