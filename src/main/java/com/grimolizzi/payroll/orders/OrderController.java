package com.grimolizzi.payroll.orders;

import com.grimolizzi.payroll.generics.GenericController;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController extends GenericController<Order> {

    public OrderController(
            JpaRepository<Order, Long> repository,
            RepresentationModelAssembler<Order, EntityModel<Order>> modelAssembler) {

        super(repository, modelAssembler);
    }

    @Override
    public ResponseEntity<?> save(@RequestBody Order inputOrder) {

        inputOrder.setStatus(Status.IN_PROGRESS);
        return super.save(inputOrder);
    }

    @PutMapping("{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {

        return this.modifyOrderStatus(id, Status.COMPLETED);
    }

    @PutMapping("{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {

        return this.modifyOrderStatus(id, Status.CANCELLED);
    }

    private ResponseEntity<?> modifyOrderStatus(Long orderId, Status status) {

        Order retrievedOrder =  this.repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (retrievedOrder.getStatus() == Status.IN_PROGRESS) {
            retrievedOrder.setStatus(status);
            this.repository.save(retrievedOrder);
            return ResponseEntity.ok(this.modelAssembler.toModel(retrievedOrder));
        } else {
            return ResponseEntity
                    .status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(new VndErrors.VndError("Method not allowed",
                            "You can't complete an order that is in the " + retrievedOrder.getStatus() + " status"));
        }
    }
}
