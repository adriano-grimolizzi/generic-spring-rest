package com.grimolizzi.payroll.employees;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data   // Lombok annotation to create all the setters, getters, equals, hash and toString methods
@Entity // JPA annotation to make the object ready for storage in a JPA-based data store
public class Employee {

    @Id                 // Primary key
    @GeneratedValue     // automatically populated by the JPA provider
    private Long id;

    private String name, role;

    public Employee() {}

    // custom constructor created when we need to create a new instance but we don't have the id.
    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }
}
