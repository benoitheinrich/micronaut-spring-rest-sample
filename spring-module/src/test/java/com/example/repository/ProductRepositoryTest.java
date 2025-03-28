package com.example.repository;

import com.example.config.TestConfig;
import com.example.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@Sql("classpath:data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldFindAllProducts() {
        Iterable<Product> products = productRepository.findAll();
        assertThat(products).hasSize(3);
    }

    @Test
    void shouldFindProductByName() {
        Optional<Product> product = productRepository.findByName("Test Product 1");
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Test Product 1");
        assertThat(product.get().getPrice()).isEqualTo(new BigDecimal("10.99"));
    }

    @Test
    void shouldSaveNewProduct() {
        Product newProduct = new Product(
                "New Product",
                "Description for new product",
                new BigDecimal("40.99")
        );

        Product savedProduct = productRepository.save(newProduct);
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("New Product");
        assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal("40.99"));

        Iterable<Product> allProducts = productRepository.findAll();
        assertThat(allProducts).hasSize(4);
    }

    @Test
    void shouldUpdateExistingProduct() {
        Optional<Product> productOpt = productRepository.findByName("Test Product 1");
        assertThat(productOpt).isPresent();

        Product product = productOpt.get();
        product.setName("Updated Product");
        product.setPrice(new BigDecimal("50.99"));

        Product updatedProduct = productRepository.save(product);
        assertThat(updatedProduct.getName()).isEqualTo("Updated Product");
        assertThat(updatedProduct.getPrice()).isEqualTo(new BigDecimal("50.99"));

        Optional<Product> foundProduct = productRepository.findById(updatedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Updated Product");
        assertThat(foundProduct.get().getPrice()).isEqualTo(new BigDecimal("50.99"));
    }

    @Test
    void shouldDeleteProduct() {
        Optional<Product> productOpt = productRepository.findByName("Test Product 1");
        assertThat(productOpt).isPresent();

        Long productId = productOpt.get().getId();
        productRepository.deleteById(productId);
        Iterable<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
        assertThat(productRepository.findById(productId)).isEmpty();
    }
} 