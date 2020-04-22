package com.grimolizzi.payroll.orders;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "CUSTOM_ORDER") // 'ORDER' is not a valid name for a table
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private String description;
    private Status status;

    public Order () {}

    public Order(String description, Status status) {
        this.description = description;
        this.status = status;
    }
}
