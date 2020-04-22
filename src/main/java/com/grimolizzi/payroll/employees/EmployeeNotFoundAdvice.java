package com.grimolizzi.payroll.employees;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmployeeNotFoundAdvice {

    @ResponseBody // the body of the advice is rendered straight into the response body
    @ExceptionHandler(EmployeeNotFoundException.class) // configure the advice to respond only if an EmployeeNotFound exception is thrown
    @ResponseStatus(HttpStatus.NOT_FOUND) // issues a 404
    String employeeNotFoundHandler(EmployeeNotFoundException exception) {
        return exception.getMessage();
    }
}
