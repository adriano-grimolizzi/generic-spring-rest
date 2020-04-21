package com.grimolizzi.payroll;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// the data returned by each method will be written straight into the response body instead of rendering a template
@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    public Iterable<Employee> findAll() {
        return repository.findAll();
    }

    @PostMapping("/employees")
    Employee save(@RequestBody Employee employee) {
        return repository.save(employee);
    }

    @GetMapping("/employees/{id}")
    Employee findById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    // TODO: other methods
    // https://spring.io/guides/tutorials/rest/
}
