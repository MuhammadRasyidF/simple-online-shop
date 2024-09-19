package com.onlineshop.project.be_order.service;


import com.onlineshop.project.be_order.dto.request.AddOrderRequest;
import com.onlineshop.project.be_order.dto.request.UpdateOrderRequest;
import com.onlineshop.project.be_order.dto.response.BaseResponse;
import com.onlineshop.project.be_order.dto.response.OrderResponse;
import com.onlineshop.project.be_order.model.Customer;
import com.onlineshop.project.be_order.model.Item;
import com.onlineshop.project.be_order.model.Order;
import com.onlineshop.project.be_order.repository.CustomerRepository;
import com.onlineshop.project.be_order.repository.ItemRepository;
import com.onlineshop.project.be_order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;;

    public BaseResponse<?> addOrder(AddOrderRequest addOrderRequest) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findById(addOrderRequest.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            return BaseResponse.builder()
                    .message("Customer tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }

        Customer customer = optionalCustomer.get();

        Optional<Item> optionalItem = itemRepository.findById(addOrderRequest.getItemId());
        if (optionalItem.isEmpty()) {
            return BaseResponse.builder()
                    .message("Item tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }

        Item item = optionalItem.get();

        if (item.getStock() < addOrderRequest.getQuantity()){
            return BaseResponse.builder()
                    .message("Stok tidak cukup untuk memenuhi pesanan")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST.name())
                    .build();
        }

        BigDecimal totalPrice = item.getPrice().multiply( new BigDecimal(addOrderRequest.getQuantity()));
        item.setStock(item.getStock() - addOrderRequest.getQuantity());
        itemRepository.save(item);

        customer.setLastOrderDate(new Date());
        customerRepository.save(customer);

        Order order = Order.builder()
                .customer(customer)
                .item(item)
                .quantity(addOrderRequest.getQuantity())
                .totalPrice(totalPrice)
                .date(new Date())
                .build();

        orderRepository.save(order);

        return BaseResponse.builder()
                .message("Berhasil menambahkan order")
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.name())
                .build();
    }

    public BaseResponse<List<OrderResponse>> getOrder(int page, int size, String search) throws Exception {

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").ascending());

        Specification<Order> spec = Specification.where(null);
        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("customer").get("name"), "%" + search + "%"));
        }

        Page<Order> orderPage = orderRepository.findAll(spec, pageable);

        List<OrderResponse> orderResponses = orderPage.stream()
                .map(order -> OrderResponse.builder()
                    .orderId(order.getId())
                    .code(order.getCode())
                    .date(order.getDate())
                    .totalPrice(order.getTotalPrice())
                    .customer(order.getCustomer())
                    .item(order.getItem())
                    .quantity(order.getQuantity())
                    .build())
                .toList();

        return BaseResponse.<List<OrderResponse>>builder()
                .data(orderResponses)
                .message("Berhasil memuat order")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public BaseResponse<OrderResponse> getOrderById(Integer id) throws Exception {

        Order order = orderRepository.findById(id).orElse(null);

        if (order== null) {
            throw new EntityNotFoundException("Order tidak ditemukan");
        }

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .code(order.getCode())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .customer(order.getCustomer())
                .item(order.getItem())
                .quantity(order.getQuantity())
                .build();

        return BaseResponse.<OrderResponse>builder()
                .data(orderResponse)
                .message("Berhasil memuat order")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public BaseResponse<?> updateOrder(Integer id, UpdateOrderRequest updateOrderRequest) throws Exception{

        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            return BaseResponse.builder()
                    .message("Order tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }

        Order order = optionalOrder.get();

        if (updateOrderRequest.getCustomerId() != null) {
            Optional<Customer> optionalCustomer = customerRepository.findById(updateOrderRequest.getCustomerId());
            if (optionalCustomer.isEmpty()) {
                return BaseResponse.builder()
                        .message("Customer tidak ditemukan")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND.name())
                        .build();
            }
            Customer customer = optionalCustomer.get();
            order.setCustomer(customer);
            customer.setLastOrderDate(new Date());
        }

        // for change quantity only
        if (updateOrderRequest.getQuantity() != null && updateOrderRequest.getItemId() == null) {
            Item item = order.getItem();
            if (item.getStock() + order.getQuantity() < updateOrderRequest.getQuantity()){
                return BaseResponse.builder()
                        .message("Stok tidak cukup untuk memenuhi pesanan")
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.name())
                        .build();
            }
            item.setStock(item.getStock() + order.getQuantity() - updateOrderRequest.getQuantity());
            itemRepository.save(item);
            order.setQuantity(updateOrderRequest.getQuantity());
            order.setTotalPrice(item.getPrice().multiply(new BigDecimal((updateOrderRequest.getQuantity()))));
        }

        // for change item and quantity
        if (updateOrderRequest.getQuantity() != null && updateOrderRequest.getItemId() != null && !Objects.equals(order.getItem().getId(), updateOrderRequest.getItemId())) {
            Optional<Item> optionalItem = itemRepository.findById(updateOrderRequest.getItemId());
            if (optionalItem.isEmpty()) {
                return BaseResponse.builder()
                        .message("Item tidak ditemukan")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND.name())
                        .build();
            }

            // revert the item stock to previous state
            Item itemBefore = order.getItem();
            itemBefore.setStock(itemBefore.getStock() + order.getQuantity());

            //  change the item
            Item itemNow = optionalItem.get();
            if (itemNow.getStock() < updateOrderRequest.getQuantity()){
                return BaseResponse.builder()
                        .message("Stok tidak cukup untuk memenuhi pesanan")
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.name())
                        .build();
            }
            itemNow.setStock(itemNow.getStock() - updateOrderRequest.getQuantity());
            itemRepository.save(itemNow);

            order.setItem(itemNow);
            order.setQuantity(updateOrderRequest.getQuantity());
            order.setTotalPrice(itemNow.getPrice().multiply(new BigDecimal((updateOrderRequest.getQuantity()))));
        }

        // for change quantity with same item
        if (updateOrderRequest.getQuantity() != null && updateOrderRequest.getItemId() != null && Objects.equals(order.getItem().getId(), updateOrderRequest.getItemId())) {
            Item item = order.getItem();
            if (item.getStock() < updateOrderRequest.getQuantity()){
                return BaseResponse.builder()
                        .message("Stok tidak cukup untuk memenuhi pesanan")
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.name())
                        .build();
            }
            item.setStock(item.getStock() + order.getQuantity() - updateOrderRequest.getQuantity());
            itemRepository.save(item);
            order.setQuantity(updateOrderRequest.getQuantity());
            order.setTotalPrice(item.getPrice().multiply(new BigDecimal((updateOrderRequest.getQuantity()))));
        }

        orderRepository.save(order);

        return BaseResponse.builder()
                .message("Berhasil memperbarui order")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .build();

    }

    public BaseResponse<?> deleteOrder(Integer orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return BaseResponse.builder()
                    .message("Order tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }

        Order order = optionalOrder.get();
        Item item = order.getItem();
        item.setStock(item.getStock() + order.getQuantity());
        itemRepository.save(item);

        orderRepository.deleteById(orderId);

        return BaseResponse.builder()
                .message("Order dengan ID " + orderId + " berhasil dihapus")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .build();
    }
}
