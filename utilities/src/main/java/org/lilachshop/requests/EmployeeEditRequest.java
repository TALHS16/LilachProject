package org.lilachshop.requests;

import org.lilachshop.entities.Employee;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EmployeeEditRequest extends Request {

    public enum Messages {
        GET_ALL_EMPLOYEES,
        SET_ALL_EMPLOYEES,
        CREATE_UPDATE_EMPLOYEE,
        DELETE_EMPLOYEES_BY_ID;
    }

    private Set<Long> idsToDelete;

    private List<Employee> employeesToEdit = null;

    private Employee employeeToEdit;

    public EmployeeEditRequest(Messages request) {
        super(request.name());
    }

    public EmployeeEditRequest(Messages request, Employee employeeToEdit) {
        super(request.name());
        this.employeeToEdit = employeeToEdit;
    }

    public EmployeeEditRequest(Messages request, List<Employee> employeesToEdit) {
        super(request.name());
        this.employeesToEdit = employeesToEdit;
    }

    public EmployeeEditRequest(Messages request, Set<Long> idsToDelete) {
        super(request.name());
        this.idsToDelete = idsToDelete;
    }

    public Set<Long> getIdsToDelete() {
        return idsToDelete;
    }

    public List<Employee> getAllEmployeesToEdit() {
        return employeesToEdit;
    }

    public Employee getEmployeeToEdit() {
        return employeeToEdit;
    }
}
