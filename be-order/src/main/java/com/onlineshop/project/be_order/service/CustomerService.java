package com.onlineshop.project.be_order.service;

import com.onlineshop.project.be_order.dto.request.AddCustomerRequest;
import com.onlineshop.project.be_order.dto.request.UpdateCustomerRequest;
import com.onlineshop.project.be_order.dto.response.BaseResponse;
import com.onlineshop.project.be_order.dto.response.CustomerResponse;
import com.onlineshop.project.be_order.model.Customer;
import com.onlineshop.project.be_order.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MinioService minioService;

    public BaseResponse<?> addCustomer(AddCustomerRequest addCustomerRequest, MultipartFile imageFile) throws Exception {

        String fileName = String.format("%s_%s.%s",
                addCustomerRequest.getCustomerName(),
                System.currentTimeMillis(),
                Objects.requireNonNull(imageFile.getContentType()).split("/")[1]);

        minioService.uploadFile(imageFile, fileName);

        Customer customer = Customer.builder()
                .name(addCustomerRequest.getCustomerName())
                .address(addCustomerRequest.getAddress())
                .phone(addCustomerRequest.getPhone())
                .isActive(true)
                .pic(fileName)
                .build();

        customerRepository.save(customer);

        return BaseResponse.<String>builder()
                .message("Berhasil menambahkan customer " + addCustomerRequest.getCustomerName())
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.name())
                .data(addCustomerRequest.getCustomerName())
                .build();
    }

    public BaseResponse<List<CustomerResponse>> getCustomer(int page, int size, String search) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Specification<Customer> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive")));

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + search + "%"));
        }

        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);

        List<CustomerResponse> customerResponses = customerPage.stream()
                .filter(Customer::getIsActive)
                .map(customer -> {
                    String pic = null;
                    if (customer.getPic() != null && !customer.getPic().isEmpty()) {
                        pic = minioService.getFileUrl(customer.getPic());
                    }

                    return CustomerResponse.builder()
                            .customerId(customer.getId())
                            .customerName(customer.getName())
                            .code(customer.getCode())
                            .address(customer.getAddress())
                            .phone(customer.getPhone())
                            .isActive(customer.getIsActive())
                            .pic(pic)
                            .build();
                })
                .toList();

        return BaseResponse.<List<CustomerResponse>>builder()
                .data(customerResponses)
                .message("Berhasil memuat customer")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();

    }

    public BaseResponse<CustomerResponse> getCustomerById(Integer id) throws Exception {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            throw new EntityNotFoundException("Customer tidak ditemukan");
        }

        if (!customer.getIsActive()) {
            throw new EntityNotFoundException("Customer sudah tidak aktif");
        }

        String pic = null;
        if (customer.getPic() != null && !customer.getPic().isEmpty()) {
            pic = minioService.getFileUrl(customer.getPic());
        }

        CustomerResponse customerResponse = CustomerResponse.builder()
                .customerId(customer.getId())
                .customerName(customer.getName())
                .code(customer.getCode())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .isActive(customer.getIsActive())
                .pic(pic)
                .build();

        return BaseResponse.<CustomerResponse>builder()
                .data(customerResponse)
                .message("Berhasil memuat customer berdasarkan id")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();

    }

    public BaseResponse<?> updateCustomer(Integer id, UpdateCustomerRequest updateCustomerRequest, MultipartFile imageFile) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            return BaseResponse.builder()
                    .message("Customer tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }

        Customer customer = optionalCustomer.get();

        if (imageFile != null) {
            //Remove old image
            minioService.removeObject(customer.getPic());

            // Upload new image and update fileName
            String fileName = String.format("%s_%s.%s",
                    updateCustomerRequest.getCustomerName(),
                    System.currentTimeMillis(),
                    Objects.requireNonNull(imageFile.getContentType()).split("/")[1]);

            minioService.uploadFile(imageFile, fileName);
            customer.setPic(fileName);
        }

        if (updateCustomerRequest.getCustomerName() != null) {
            customer.setName(updateCustomerRequest.getCustomerName());
        }
        if (updateCustomerRequest.getAddress() != null) {
            customer.setAddress(updateCustomerRequest.getAddress());
        }
        if (updateCustomerRequest.getPhone() != null) {
            customer.setPhone(updateCustomerRequest.getPhone());
        }

        customerRepository.save(customer);

        return BaseResponse.builder()
                .message("Berhasil memperbarui customer " + customer.getName())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .build();
    }

    public BaseResponse<?> deleteCustomer(Integer customerId) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if(optionalCustomer.isEmpty()){
            return BaseResponse.builder()
                    .message("Customer tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }
        Customer customer = optionalCustomer.get();

        //Remove image
        if (customer.getPic() != null && !customer.getPic().isEmpty()) {
                minioService.removeObject(customer.getPic());
        }

        customerRepository.updateIsActiveById(customerId, false);

        return BaseResponse.<String>builder()
                .message("Customer dengan ID " + customerId + " berhasil dihapus")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .build();

    }

}
