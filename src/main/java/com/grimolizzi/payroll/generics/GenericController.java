package com.grimolizzi.payroll.generics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class GenericController<T> {

    protected JpaRepository<T, Long> repository;
    protected RepresentationModelAssembler<T, EntityModel<T>> modelAssembler;

    public GenericController(
            JpaRepository<T, Long> repository,
            RepresentationModelAssembler<T, EntityModel<T>> modelAssembler) {
        this.repository = repository;
        this.modelAssembler = modelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<T>> findAll() {

        List<EntityModel<T>> returnList = this.repository.findAll().stream()
                .map(this.modelAssembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(returnList,
                linkTo(methodOn(GenericController.class).findAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<T> findById(@PathVariable Long id) {

        T t = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find resource with id: " + id));

        return this.modelAssembler.toModel(t);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody T t) {

        EntityModel<T> entityModel = this.modelAssembler.toModel(this.repository.save(t));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
