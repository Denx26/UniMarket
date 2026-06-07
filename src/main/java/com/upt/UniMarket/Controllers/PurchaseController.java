package com.upt.UniMarket.Controllers;

import com.upt.UniMarket.Dto.PurchaseRequest;
import com.upt.UniMarket.Dto.MessageResponse;
import com.upt.UniMarket.Entity.Product;
import com.upt.UniMarket.Entity.Transaction;
import com.upt.UniMarket.Entity.User;
import com.upt.UniMarket.Repositories.ProductRepository;
import com.upt.UniMarket.Repositories.TransactionRepository;
import com.upt.UniMarket.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PurchaseController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Get all available products for the storefront showcase
    @GetMapping("/products/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        List<Product> availableProducts = productRepository.findAll();
        return ResponseEntity.ok(availableProducts);
    }

    // 2. Buy a product: Create a transaction record and delete the product atomically
    @PostMapping("/purchase/{productId}")
    @Transactional
    public ResponseEntity<?> purchaseProduct(@PathVariable Long productId, @RequestBody PurchaseRequest request) {
        // Find the product targeted for purchase
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("The selected product is no longer available!"));
        }
        Product product = productOpt.get();

        // Cross-reference user table to extract the seller's email address
        Optional<User> sellerOpt = userRepository.findById(product.getVanzatorId());
        String sellerEmail = sellerOpt.map(User::getEmail).orElse("unknown.seller@email.com");

        // Construct the transactional history record
        Transaction transaction = new Transaction(
                product.getNume(),
                product.getPret(),
                request.getBuyerEmail(),
                sellerEmail
        );

        // Perform the safe transactional operations
        transactionRepository.save(transaction);
        productRepository.deleteById(productId);

        return ResponseEntity.ok(new MessageResponse("Purchase completed successfully! Product removed from showcase."));
    }

    // 3. View the comprehensive sales transaction history log
    @GetMapping("/purchase/history")
    public ResponseEntity<List<Transaction>> getPurchaseHistory() {
        List<Transaction> history = transactionRepository.findAll();
        return ResponseEntity.ok(history);
    }
}