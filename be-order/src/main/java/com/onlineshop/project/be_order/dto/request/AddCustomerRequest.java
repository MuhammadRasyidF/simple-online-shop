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

    private Integer customerId;

    @NotBlank(message = " Kolom customer_name tidak boleh kosong")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Kolom tidak boleh berisi special character/angka")
    private String customerName;

    private String address;

    private UUID code = UUID.randomUUID();

    private String phone;

    private Boolean isActive;

    private Date lastOrderDate;

    private String pic;
}
