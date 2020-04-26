package com.grimolizzi.payroll.employees;

import com.grimolizzi.payroll.generics.GenericController;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends GenericController<Employee> {

    public EmployeeController(
            JpaRepository<Employee, Long> repository,
            RepresentationModelAssembler<Employee, EntityModel<Employee>> modelAssembler) {
        super(repository, modelAssembler);
    }

    // TODO: other methods
    // https://spring.io/guides/tutorials/rest/
}
