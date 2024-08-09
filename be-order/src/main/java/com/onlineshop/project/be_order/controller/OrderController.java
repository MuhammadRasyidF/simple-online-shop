package com.onlineshop.project.be_order.controller;

import com.onlineshop.project.be_order.dto.request.AddOrderRequest;
import com.onlineshop.project.be_order.dto.request.UpdateOrderRequest;
import com.onlineshop.project.be_order.dto.response.BaseResponse;
import com.onlineshop.project.be_order.dto.response.OrderResponse;
import com.onlineshop.project.be_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> addOrder(
            @RequestBody AddOrderRequest addOrderRequest) throws Exception {

        BaseResponse<?> response = orderService.addOrder(addOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getOrder() throws Exception {
        BaseResponse<List<OrderResponse>> response = orderService.getOrder();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping(value = "/{id}/edit")
    public ResponseEntity<?> updateOrder(
            @PathVariable Integer id,
            @RequestBody UpdateOrderRequest updateOrderRequest) throws Exception {

        BaseResponse<?> response = orderService.updateOrder(id, updateOrderRequest);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) throws Exception {

        BaseResponse<?> response = orderService.deleteOrder(id);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
