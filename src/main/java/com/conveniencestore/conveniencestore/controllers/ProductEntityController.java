package com.conveniencestore.conveniencestore.controllers;

import com.conveniencestore.conveniencestore.domain.ProductEntity.ProductEntity;
import com.conveniencestore.conveniencestore.domain.ProductEntity.ProductEntityDTO;
import com.conveniencestore.conveniencestore.domain.ProductEntity.exceptions.ProductEntityNotFoundException;
import com.conveniencestore.conveniencestore.services.ProductEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("products/entities")
public class ProductEntityController {
    private final ProductEntityService service;
    private static final List<String> VALID_SEARCH_PARAMETERS = List.of("id", "name", "asc", "desc");

    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(required = false)
            String orderby,
            @RequestParam(required = false)
            String order
            ) {
        String sortField = Optional.ofNullable(orderby).orElse("id");
        String sortOrder = Optional.ofNullable(order).orElse("asc");
        if(VALID_SEARCH_PARAMETERS.contains(sortField) && VALID_SEARCH_PARAMETERS.contains(sortOrder)) return ResponseEntity.ok().body(this.service.getAll(sortField, sortOrder));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        if (id == null) return ResponseEntity.badRequest().build();
        try {
            ProductEntity productEntity = this.service.getById(id);
            return ResponseEntity.ok().body(productEntity);
        } catch (ProductEntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<?> registerNewProduct(@RequestBody @Valid ProductEntityDTO request) {
        ProductEntity productEntity = this.service.insert(request);
        return ResponseEntity.status(201).body(productEntity);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductEntityDTO productEntityRecord) {
        if (id == null) return ResponseEntity.badRequest().build();
        try {
            ProductEntity productEntity = this.service.update(id, productEntityRecord);
            return ResponseEntity.ok().body(productEntity);
        } catch (ProductEntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        if (id == null) return ResponseEntity.badRequest().build();
        try {
            ProductEntity productEntity = this.service.delete(id);
            return ResponseEntity.ok().body(productEntity);
        } catch (ProductEntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
