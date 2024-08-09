package com.onlineshop.project.be_order.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddItemRequest {

    @NotBlank(message = " Kolom item_name tidak boleh kosong")
    private String itemName;

    private Integer stock;

    private BigDecimal price;

}
