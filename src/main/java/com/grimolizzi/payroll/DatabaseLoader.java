package com.grimolizzi.payroll;

import com.grimolizzi.payroll.employees.Employee;
import com.grimolizzi.payroll.employees.EmployeeRepository;
import com.grimolizzi.payroll.orders.Order;
import com.grimolizzi.payroll.orders.OrderRepository;
import com.grimolizzi.payroll.orders.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j         // autocreates a LoggerFactory as log
public class DatabaseLoader {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee("Albus Silente", "Headmaster")));
            log.info("Preloading " + employeeRepository.save(new Employee("Draco Malfoy", "Student")));

            orderRepository.save(new Order("Sting", Status.IN_PROGRESS));
            orderRepository.save(new Order("Glamdring", Status.COMPLETED));
            orderRepository.save(new Order("Orcrist", Status.CANCELLED));

            orderRepository.findAll().forEach(order -> log.info("Preload " + order));
        };
    }
}
