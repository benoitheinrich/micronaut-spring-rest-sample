package com.example.controller;

import com.example.model.Product;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
class ProductControllerValidationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testCreateProductWithEmptyName() {
        Product product = new Product("", "Test Description", new BigDecimal("19.99"));
        HttpRequest<Product> request = HttpRequest.POST("/products", product);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request, Product.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreateProductWithNullName() {
        Product product = new Product(null, "Test Description", new BigDecimal("19.99"));
        HttpRequest<Product> request = HttpRequest.POST("/products", product);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request, Product.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreateProductWithNegativePrice() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("-1.0"));
        HttpRequest<Product> request = HttpRequest.POST("/products", product);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request, Product.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreateProductWithZeroPrice() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("0.0"));
        HttpRequest<Product> request = HttpRequest.POST("/products", product);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request, Product.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreateProductWithNullPrice() {
        Product product = new Product("Test Product", "Test Description", null);
        HttpRequest<Product> request = HttpRequest.POST("/products", product);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request, Product.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testUpdateProductWithInvalidData() {
        // First create a valid product
        Product product = new Product("Test Product", "Test Description", new BigDecimal("19.99"));
        HttpRequest<Product> createRequest = HttpRequest.POST("/products", product);
        Product createdProduct = client.toBlocking().retrieve(createRequest, Product.class);

        // Now try to update with invalid data
        Product invalidProduct = new Product("", "Updated Description", new BigDecimal("0.0"));
        HttpRequest<Product> updateRequest = HttpRequest.PUT("/products/" + createdProduct.getId(), invalidProduct);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(updateRequest, Product.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
} 