package com.example.repository;

import com.example.model.Product;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ProductRepositoryMicronaut extends ProductRepository, CrudRepository<Product, Long> {
}
