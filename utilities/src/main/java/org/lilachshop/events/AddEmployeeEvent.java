package org.lilachshop.events;

import org.lilachshop.entities.Employee;

import java.io.Serializable;

public class AddEmployeeEvent implements Serializable {
    Employee employee;

    public AddEmployeeEvent(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }
}
