package com.onlineshop.project.be_order.service;

import com.onlineshop.project.be_order.dto.request.AddItemRequest;
import com.onlineshop.project.be_order.dto.request.UpdateItemRequest;
import com.onlineshop.project.be_order.dto.response.BaseResponse;
import com.onlineshop.project.be_order.dto.response.ItemResponse;
import com.onlineshop.project.be_order.model.Item;
import com.onlineshop.project.be_order.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public BaseResponse<?> addItem(AddItemRequest addItemRequest) throws Exception {
        Item item = Item.builder()
                .name(addItemRequest.getItemName())
                .stock(addItemRequest.getStock())
                .price(addItemRequest.getPrice())
                .isAvailable(true)
                .build();

        itemRepository.save(item);

        return BaseResponse.<String>builder()
                .message("Berhasil menambahkan customer " + addItemRequest.getItemName())
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.ACCEPTED.name())
                .data(addItemRequest.getItemName())
                .build();
    }

    public BaseResponse<List<ItemResponse>> getItem(int page, int size, String search) throws Exception {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Specification<Item> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isAvailable")));

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + search + "%"));
        }

        Page<Item> itemsPage = itemRepository.findAll(spec, pageable);

        List<ItemResponse> itemResponses = itemsPage.stream()
                .map(item -> ItemResponse.builder()
                        .itemId(item.getId())
                        .itemName(item.getName())
                        .code(item.getCode())
                        .stock(item.getStock())
                        .price(item.getPrice())
                        .isAvailable(item.getIsAvailable())
                        .lastReStock(item.getLastReStock())
                        .build())
                .toList();

        return BaseResponse.<List<ItemResponse>>builder()
                .data(itemResponses)
                .message("Berhasil memuat item")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public BaseResponse<ItemResponse> getItemById(Integer id) throws Exception {
        Item item = itemRepository.findById(id).orElse(null);

        if (item == null) {
            throw new EntityNotFoundException("Item tidak ditemukan");
        }

        if (!item.getIsAvailable()) {
            throw new EntityNotFoundException("Item sudah dihapus");
        }

        ItemResponse itemResponse = ItemResponse.builder()
                .itemId(item.getId())
                .itemName(item.getName())
                .code(item.getCode())
                .stock(item.getStock())
                .price(item.getPrice())
                .isAvailable(item.getIsAvailable())
                .lastReStock(item.getLastReStock())
                .build();

        return BaseResponse.<ItemResponse>builder()
                .data(itemResponse)
                .message("Berhasil memuat item")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public BaseResponse<?> updateItem(Integer id, UpdateItemRequest updateItemRequest) throws Exception {

        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            return BaseResponse.builder()
                    .message("Item tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }

        Item item = optionalItem.get();

        if (updateItemRequest.getItemName() != null) {
            item.setName(updateItemRequest.getItemName());
        }

        if (updateItemRequest.getStock() != null) {
            item.setStock(updateItemRequest.getStock());
            item.setLastReStock(new Date());
        }

        if (updateItemRequest.getPrice() != null) {
            item.setPrice(updateItemRequest.getPrice());
        }

        if (updateItemRequest.getIsAvailable() != null) {
            item.setIsAvailable(updateItemRequest.getIsAvailable());
        }

        itemRepository.save(item);

        return BaseResponse.<String>builder()
                .message("Berhasil memperbarui item " + item.getName())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data("Last restock: " + new Date())
                .build();
    }

    public BaseResponse<?> addStock(Integer id, UpdateItemRequest updateItemRequest) throws Exception {

        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            return BaseResponse.builder()
                    .message("Item tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }

        Item item = optionalItem.get();


        if (updateItemRequest.getStock() != null) {
            item.setStock(updateItemRequest.getStock());
            item.setLastReStock(new Date());
        }

        itemRepository.save(item);

        return BaseResponse.<String>builder()
                .message("Berhasil memperbarui stock item " + item.getName())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data("Last restock: " + new Date())
                .build();
    }

    public BaseResponse<?> deleteItem(Integer itemId) throws Exception {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            return BaseResponse.builder()
                    .message("Item tidak ditemukan")
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND.name())
                    .build();
        }

        Item item = optionalItem.get();

        itemRepository.deleteById(itemId);

        return BaseResponse.<String>builder()
                .message("Item dengan ID " + itemId + " berhasil dihapus")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .build();

    }

}
