package com.grimolizzi.payroll.employees;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// the data returned by each method will be written straight into the response body instead of rendering a template
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Employee>> findAll() {

        List<EntityModel<Employee>> employees = this.repository.findAll().stream()
                .map(this.assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).findAll()).withSelfRel());
    }

    @PostMapping
    ResponseEntity<?> save(@RequestBody Employee employee) {

        EntityModel<Employee> entityModel = this.assembler.toModel(this.repository.save(employee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> findById(@PathVariable Long id) {

        // Entity Model is a generic container from Spring HATEOAS
        // that includes not only the data but a collection of links.
        Employee employee =  this.repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return this.assembler.toModel(employee);
    }

    // TODO: other methods
    // https://spring.io/guides/tutorials/rest/
}
