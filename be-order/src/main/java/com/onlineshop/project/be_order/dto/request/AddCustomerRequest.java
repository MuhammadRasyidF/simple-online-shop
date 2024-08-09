package com.onlineshop.project.be_order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class AddCustomerRequest {

    @NotBlank(message = " Kolom customer_name tidak boleh kosong")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Kolom tidak boleh berisi special character/angka")
    private String customerName;

    private String address;

    private String phone;

    private Date lastOrderDate;

    private String pic;
}
