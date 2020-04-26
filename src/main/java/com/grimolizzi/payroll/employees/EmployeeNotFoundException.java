package com.grimolizzi.payroll.employees;

// This Exception extends RuntimeException: so it's  unchecked.
// Note that the controller method that uses it doesn't throw it.
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
