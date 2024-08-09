package com.onlineshop.project.be_order.controller;

import com.onlineshop.project.be_order.dto.request.AddCustomerRequest;
import com.onlineshop.project.be_order.dto.request.UpdateCustomerRequest;
import com.onlineshop.project.be_order.dto.response.BaseResponse;
import com.onlineshop.project.be_order.dto.response.CustomerRespone;
import com.onlineshop.project.be_order.service.CustomerService;
import io.minio.errors.MinioException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addCustomer(
            @Valid
            @RequestPart("data") AddCustomerRequest addCustomerRequest,
            @RequestPart("image")MultipartFile imageFile) throws Exception {

        if (!List.of("image/jpeg", "image/jpg", "image/png").contains(imageFile.getContentType())) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message("Format gambar tidak sesuai (jpg/jpeg/png)")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .build());
        }

        BaseResponse<?> response = customerService.addCustomer(addCustomerRequest, imageFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getCustomer() throws Exception {
        BaseResponse<List<CustomerRespone>> response = customerService.getCustomer();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping(value = "/{id}/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateCustomer(
            @Valid
            @PathVariable Integer id,
            @RequestPart("data") UpdateCustomerRequest updateCustomerRequest,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws Exception {

        if (imageFile != null && !List.of("image/jpeg", "image/jpg", "image/png").contains(imageFile.getContentType())) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message("Format gambar tidak sesuai (jpg/jpeg/png)")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .build());
        }

        BaseResponse<?> response = customerService.updateCustomer(id, updateCustomerRequest, imageFile);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) throws Exception {

        BaseResponse<?> response = customerService.deleteCustomer(id);

        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

}
