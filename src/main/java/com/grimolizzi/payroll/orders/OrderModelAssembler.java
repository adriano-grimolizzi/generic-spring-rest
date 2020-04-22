package com.grimolizzi.payroll.orders;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order inputOrder) {

        EntityModel<Order> orderModel = new EntityModel<>(inputOrder,
                linkTo(methodOn(OrderController.class).findById(inputOrder.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).findAll()).withRel("orders"));

        // conditional link based on the status of the order
        if (inputOrder.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(
                    linkTo(methodOn(OrderController.class).complete(inputOrder.getId())).withRel("complete"),
                    linkTo(methodOn(OrderController.class).cancel(inputOrder.getId())).withRel("cancel"));
        }

        return orderModel;
    }
}
