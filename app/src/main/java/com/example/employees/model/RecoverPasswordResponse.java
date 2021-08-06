
package com.example.employees.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecoverPasswordResponse implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("password")
    @Expose
    private String password;
    private final static long serialVersionUID = -4532472568266485782L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
