package com.upt.UniMarket.Repositories;

import com.upt.UniMarket.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByVanzatorId(long vanzatorId);
}
