package com.example.controller;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Get
    public List<Product> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Get("/{id}")
    public HttpResponse<Product> getProduct(Long id) {
        return productRepository.findById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Product addProduct(@Body @Valid Product product) {
        return productRepository.save(product);
    }

    @Put("/{id}")
    @Transactional
    public HttpResponse<Product> updateProduct(Long id, @Body @Valid Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    return HttpResponse.ok(productRepository.save(existingProduct));
                })
                .orElse(HttpResponse.notFound());
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
} 