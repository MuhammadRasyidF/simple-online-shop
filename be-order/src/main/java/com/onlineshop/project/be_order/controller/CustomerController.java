package com.onlineshop.project.be_order.controller;

import com.onlineshop.project.be_order.dto.request.AddCustomerRequest;
import com.onlineshop.project.be_order.dto.request.UpdateCustomerRequest;
import com.onlineshop.project.be_order.dto.response.BaseResponse;
import com.onlineshop.project.be_order.dto.response.CustomerResponse;
import com.onlineshop.project.be_order.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        if (imageFile.getContentType() == null){
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message("Tolong upload gambar")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .build());
        }

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
    public ResponseEntity<?> getCustomer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam (required = false) String search) throws Exception {

        BaseResponse<List<CustomerResponse>> response = customerService.getCustomer(page, size, search);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCustomerById(
            @PathVariable Integer id) throws Exception {

        BaseResponse<CustomerResponse> response = customerService.getCustomerById(id);

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
