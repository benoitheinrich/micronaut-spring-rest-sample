package com.example.controller;

import com.example.model.Product;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
class ProductControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    private Product createProduct() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("19.99"));
        HttpRequest<Product> request = HttpRequest.POST("/products", product);
        return client.toBlocking().retrieve(request, Product.class);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("19.99"));
        HttpRequest<Product> request = HttpRequest.POST("/products", product);
        HttpResponse<Product> response = client.toBlocking().exchange(request, Product.class);

        assertEquals(HttpStatus.CREATED, response.status());
        assertNotNull(response.body());
        assertNotNull(response.body().getId());
        assertEquals("Test Product", response.body().getName());
        assertEquals("Test Description", response.body().getDescription());
        assertEquals(0, new BigDecimal("19.99").compareTo(response.body().getPrice()));
    }

    @Test
    void testGetAllProducts() {
        // Create a product first
        createProduct();

        HttpRequest<Object> request = HttpRequest.GET("/products");
        List<Product> products = client.toBlocking().retrieve(request, Argument.listOf(Product.class));

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void testGetProductById() {
        // Create a product first
        Product product = createProduct();

        HttpRequest<Object> request = HttpRequest.GET("/products/" + product.getId());
        Product retrievedProduct = client.toBlocking().retrieve(request, Product.class);

        assertNotNull(retrievedProduct);
        assertEquals(product.getId(), retrievedProduct.getId());
        assertEquals(product.getName(), retrievedProduct.getName());
        assertEquals(product.getDescription(), retrievedProduct.getDescription());
        assertEquals(0, product.getPrice().compareTo(retrievedProduct.getPrice()));
    }

    @Test
    void testGetProductByIdNotFound() {
        HttpRequest<Object> request = HttpRequest.GET("/products/9999");

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(request, Product.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testUpdateProduct() {
        // Create a product first
        Product product = createProduct();

        // Update the product
        Product updatedProduct = new Product("Updated Product", "Updated Description", new BigDecimal("29.99"));
        HttpRequest<Product> request = HttpRequest.PUT("/products/" + product.getId(), updatedProduct);
        HttpResponse<Product> response = client.toBlocking().exchange(request, Product.class);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(product.getId(), response.body().getId());
        assertEquals("Updated Product", response.body().getName());
        assertEquals("Updated Description", response.body().getDescription());
        assertEquals(0, new BigDecimal("29.99").compareTo(response.body().getPrice()));
    }

    @Test
    void testUpdateProductNotFound() {
        Product updatedProduct = new Product("Updated Product", "Updated Description", new BigDecimal("29.99"));
        HttpRequest<Product> request = HttpRequest.PUT("/products/9999", updatedProduct);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request, Product.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testDeleteProduct() {
        // Create a product first
        Product product = createProduct();

        // Delete the product
        HttpRequest<Object> deleteRequest = HttpRequest.DELETE("/products/" + product.getId());
        HttpResponse<Object> deleteResponse = client.toBlocking().exchange(deleteRequest);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status());

        // Verify it's deleted
        HttpRequest<Object> getRequest = HttpRequest.GET("/products/" + product.getId());

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(getRequest, Product.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
} 