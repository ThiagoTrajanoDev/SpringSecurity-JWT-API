package com.SecurityAplication.AutenticationApp.repository;

import com.SecurityAplication.AutenticationApp.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
