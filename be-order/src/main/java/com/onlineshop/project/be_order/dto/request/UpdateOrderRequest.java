package com.onlineshop.project.be_order.dto.request;

import com.onlineshop.project.be_order.model.Customer;
import com.onlineshop.project.be_order.model.Item;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateOrderRequest {

    private Integer customerId;

    private Integer itemId;

    private  Integer quantity;

}
