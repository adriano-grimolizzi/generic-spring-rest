package com.grimolizzi.payroll;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

// the data returned by each method will be written straight into the response body instead of rendering a template
@RestController
public class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // TODO: change return type to CollectionModel<Employee>
    // https://spring.io/guides/tutorials/rest/
    @GetMapping("/employees")
    public List<EntityModel<Employee>> findAll() {

        return this.repository.findAll().stream()
                .map(this.assembler::toModel)
                .collect(Collectors.toList());
    }

    @PostMapping("/employees")
    ResponseEntity<?> save(@RequestBody Employee employee) {

        EntityModel<Employee> entityModel = this.assembler.toModel(this.repository.save(employee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> findById(@PathVariable Long id) {

        Employee employee =  this.repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return this.assembler.toModel(employee);
    }

    // TODO: other methods
    // https://spring.io/guides/tutorials/rest/
}

/*
/ Entity Model is a generic container from Spring HATEOAS that includes not only the data but a collection of links.
*/
