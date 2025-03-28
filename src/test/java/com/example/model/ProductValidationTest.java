package com.example.model;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class ProductValidationTest {

    @Inject
    private Validator validator;

    @Test
    void testValidProduct() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("19.99"));
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNameRequired() {
        Product product = new Product(null, "Test Description", new BigDecimal("19.99"));
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        
        assertEquals(1, violations.size());
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Name is required", violation.getMessage());
    }

    @Test
    void testNameTooLong() {
        // Create a name with 101 characters
        String longName = "X".repeat(101);
        
        Product product = new Product(longName, "Test Description", new BigDecimal("19.99"));
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        
        assertEquals(1, violations.size());
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Name cannot exceed 100 characters", violation.getMessage());
    }

    @Test
    void testDescriptionTooLong() {
        // Create a description with 501 characters
        String longDescription = "X".repeat(501);
        
        Product product = new Product("Test Product", longDescription, new BigDecimal("19.99"));
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        
        assertEquals(1, violations.size());
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("Description cannot exceed 500 characters", violation.getMessage());
    }

    @Test
    void testPriceRequired() {
        Product product = new Product("Test Product", "Test Description", null);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        
        assertEquals(1, violations.size());
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("price", violation.getPropertyPath().toString());
        assertEquals("Price is required", violation.getMessage());
    }

    @Test
    void testPriceGreaterThanZero() {
        Product product = new Product("Test Product", "Test Description", new BigDecimal("0.0"));
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        
        assertEquals(1, violations.size());
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("price", violation.getPropertyPath().toString());
        assertEquals("Price must be greater than 0", violation.getMessage());
    }

    @Test
    void testMultipleViolations() {
        Product product = new Product("", null, new BigDecimal("-5.0"));
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        
        assertEquals(2, violations.size());
        // Check that we have violations for name and price
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    }
} 