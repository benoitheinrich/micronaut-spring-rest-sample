package com.example.controller;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
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
    public HttpResponse<Product> updateProduct(Long id, @Body @Valid Product product) {
        if (!productRepository.existsById(id)) {
            return HttpResponse.notFound();
        }
        product.setId(id);
        Product updatedProduct = productRepository.update(product);
        return HttpResponse.ok(updatedProduct);
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
} 