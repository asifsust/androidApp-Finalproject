
package com.example.employees.model.employeeUpdate;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeUpdateResponse implements Serializable
{

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("employee")
    @Expose
    private Employee employee;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

}
