package com.grimolizzi.payroll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j         // autocreates a LoggerFactory as log
public class DatabaseLoader {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Employee("Albus Silente", "Headmaster")));
            log.info("Preloading " + repository.save(new Employee("Draco Malfoy", "Student")));
        };
    }
}
