package org.lilachshop.events;

import org.lilachshop.entities.Employee;

public class AddItemEvent {
    Employee employee;

    public AddItemEvent(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }
}
