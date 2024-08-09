package com.onlineshop.project.be_order.controller;

import com.onlineshop.project.be_order.dto.request.AddCustomerRequest;
import com.onlineshop.project.be_order.dto.request.AddItemRequest;
import com.onlineshop.project.be_order.dto.request.UpdateCustomerRequest;
import com.onlineshop.project.be_order.dto.request.UpdateItemRequest;
import com.onlineshop.project.be_order.dto.response.BaseResponse;
import com.onlineshop.project.be_order.dto.response.CustomerRespone;
import com.onlineshop.project.be_order.dto.response.ItemResponse;
import com.onlineshop.project.be_order.service.CustomerService;
import com.onlineshop.project.be_order.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> addItem(
            @Valid
            @RequestBody AddItemRequest addItemRequest) throws Exception {

        BaseResponse<?> response = itemService.addItem(addItemRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getItem() throws Exception {
        BaseResponse<List<ItemResponse>> response = itemService.getItem();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping(value = "/{id}/edit")
    public ResponseEntity<?> updateItem(
            @Valid
            @PathVariable Integer id,
            @RequestBody UpdateItemRequest updateItemRequest) throws Exception {

        BaseResponse<?> response = itemService.updateItem(id, updateItemRequest);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping(value = "/{id}/addStock")
    public ResponseEntity<?> addStock(
            @Valid
            @PathVariable Integer id,
            @RequestBody UpdateItemRequest updateItemRequest) throws Exception {

        BaseResponse<?> response = itemService.addStock(id, updateItemRequest);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteItem(@PathVariable Integer id) throws Exception {

        BaseResponse<?> response = itemService.deleteItem(id);

        return ResponseEntity.status(response.getStatusCode()).body(response);

    }
}
