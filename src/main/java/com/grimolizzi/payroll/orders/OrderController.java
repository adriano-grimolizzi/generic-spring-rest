package com.grimolizzi.payroll.orders;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repository;
    private final OrderModelAssembler assembler;

    public OrderController(OrderRepository repository, OrderModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    CollectionModel<EntityModel<Order>> findAll() {

         List<EntityModel<Order>> orders = repository.findAll().stream()
                 .map(assembler::toModel)
                 .collect(Collectors.toList());

         return new CollectionModel<>(orders,
                 linkTo(methodOn(OrderController.class).findAll()).withSelfRel());
     }

    @GetMapping("/{id}")
    EntityModel<Order> findById(@PathVariable Long id) {

        Order order =  this.repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return this.assembler.toModel(order);
    }

    @PostMapping
    ResponseEntity<?> save(@RequestBody Order inputOrder) {

        inputOrder.setStatus(Status.IN_PROGRESS);
        Order newOrder = this.repository.save(inputOrder);

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).findById(newOrder.getId())).toUri())
                .body(this.assembler.toModel(newOrder));
    }

    @PutMapping("{id}/complete")
    ResponseEntity<RepresentationModel> complete(@PathVariable Long id) {

        return this.modifyOrderStatus(id, Status.COMPLETED);
    }

    @PutMapping("{id}/cancel")
    ResponseEntity<RepresentationModel> cancel(@PathVariable Long id) {

        return this.modifyOrderStatus(id, Status.CANCELLED);
    }

    private ResponseEntity<RepresentationModel> modifyOrderStatus(Long orderId, Status status) {

        Order retrievedOrder =  this.repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (retrievedOrder.getStatus() == Status.IN_PROGRESS) {
            retrievedOrder.setStatus(status);
            this.repository.save(retrievedOrder);
            return ResponseEntity.ok(this.assembler.toModel(retrievedOrder));
        } else {
            return ResponseEntity
                    .status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(new VndErrors.VndError("Method not allowed",
                            "You can't complete an order that is in the " + retrievedOrder.getStatus() + " status"));
        }
    }
}
