package com.onlineshop.project.be_order.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCustomerRequest {

    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Kolom tidak boleh berisi special character/angka")
    private String customerName;

    private String address;

    private String phone;

    private Boolean isActive;

    private String pic;
}

