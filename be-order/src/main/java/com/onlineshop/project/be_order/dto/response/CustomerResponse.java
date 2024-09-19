package com.onlineshop.project.be_order.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {
    private Integer customerId;

    private String customerName;

    private String address;

    private UUID code;

    private String phone;

    private Boolean isActive;

    private Date lastOrderDate;

    private String pic;
}
