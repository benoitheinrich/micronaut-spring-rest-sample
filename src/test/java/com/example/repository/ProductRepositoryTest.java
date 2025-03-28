package com.example.repository;

import com.example.model.Product;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class ProductRepositoryTest {

    @Inject
    ProductRepository productRepository;

    @Test
    void testSaveProduct() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("19.99"));
        Product savedProduct = productRepository.save(product);
        
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getName());
        assertEquals("Test Description", savedProduct.getDescription());
        assertEquals(0, new BigDecimal("19.99").compareTo(savedProduct.getPrice()));
    }

    @Test
    void testFindById() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("19.99"));
        Product savedProduct = productRepository.save(product);
        
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        
        assertTrue(foundProduct.isPresent());
        assertEquals(savedProduct.getId(), foundProduct.get().getId());
        assertEquals("Test Product", foundProduct.get().getName());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("19.99"));
        Product savedProduct = productRepository.save(product);
        
        savedProduct.setName("Updated Product");
        savedProduct.setDescription("Updated Description");
        savedProduct.setPrice(new BigDecimal("29.99"));
        
        Product updatedProduct = productRepository.update(savedProduct);
        
        assertEquals(savedProduct.getId(), updatedProduct.getId());
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(0, new BigDecimal("29.99").compareTo(updatedProduct.getPrice()));
    }

    @Test
    void testDeleteById() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("19.99"));
        Product savedProduct = productRepository.save(product);
        
        productRepository.deleteById(savedProduct.getId());
        
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        assertFalse(foundProduct.isPresent());
    }

    @Test
    void testFindAll() {
        // Clear any existing data
        productRepository.deleteAll();
        
        // Add two products
        productRepository.save(new Product("Product 1", "Description 1", new BigDecimal("19.99")));
        productRepository.save(new Product("Product 2", "Description 2", new BigDecimal("29.99")));
        
        // Find all products
        Iterable<Product> products = productRepository.findAll();
        
        // Count the products
        long count = 0;
        for (Product ignored : products) {
            count++;
        }
        
        assertEquals(2, count);
    }
} 