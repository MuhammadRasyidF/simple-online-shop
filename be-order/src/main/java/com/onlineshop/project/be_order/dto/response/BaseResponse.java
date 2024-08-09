package com.onlineshop.project.be_order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class BaseResponse<T> {

    private String message;

    private int statusCode;

    private String status;

    private T data;

}
