package com.upt.UniMarket.Controllers;

import com.upt.UniMarket.Entity.Product;
import com.upt.UniMarket.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produse")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/")
    public void addProduct(@RequestBody Product product) {
        productRepository.save(product);
    }
    @PutMapping("/{id}")
    public void editProduct(@RequestBody Product product, @PathVariable long id) {
        product.setPid(id);
        productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id) {
        productRepository.deleteById(id);
    }

    @GetMapping("/vanzator/{id}")
    public List<Product> findByVanzatorId(@PathVariable long id) {
        return productRepository.findByVanzatorId(id);
    }

}
