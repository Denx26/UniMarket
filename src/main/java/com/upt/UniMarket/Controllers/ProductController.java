
package com.upt.UniMarket.Controllers;

import com.upt.UniMarket.Dto.MessageResponse;
import com.upt.UniMarket.Entity.Product;
import com.upt.UniMarket.Entity.Seller;
import com.upt.UniMarket.Entity.SellerStatus;
import com.upt.UniMarket.Repositories.ProductRepository;
import com.upt.UniMarket.Repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produse")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @PostMapping("/")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        Long sellerId = product.getVanzatorId();
        Optional<Seller> sellerOpt = (sellerId != null) ? sellerRepository.findById(sellerId) : Optional.empty();

        if (sellerOpt.isEmpty() || sellerOpt.get().getAccountStatus() != SellerStatus.ACTIVE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Doar vanzatorii activi (aprobati de admin) pot adauga produse."));
        }

        productRepository.save(product);
        return ResponseEntity.ok(new MessageResponse("Produs adaugat."));
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

