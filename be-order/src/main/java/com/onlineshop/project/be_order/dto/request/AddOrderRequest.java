package com.onlineshop.project.be_order.dto.request;

import com.onlineshop.project.be_order.model.Customer;
import com.onlineshop.project.be_order.model.Item;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddOrderRequest {

    @NotNull(message = "kolom customer tidak boleh kosong")
    private Integer customerId;

    @NotNull(message = "kolom item tidak boleh kosong")
    private Integer itemId;

    @NotNull(message = "kolom quantity tidak boleh kosong")
    private  Integer quantity;

}
