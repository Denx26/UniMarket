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

    @GetMapping("/products/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        List<Product> availableProducts = productRepository.findAll();
        return ResponseEntity.ok(availableProducts);
    }

    @PostMapping("/purchase/{productId}")
    @Transactional
    public ResponseEntity<?> purchaseProduct(@PathVariable Long productId, @RequestBody PurchaseRequest request) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Produsul selectat nu mai este disponibil!"));
        }
        Product product = productOpt.get();

        Optional<User> sellerOpt = userRepository.findById(product.getVanzatorId());
        String sellerEmail = sellerOpt.map(User::getEmail).orElse("unknown.seller@email.com");

        Transaction transaction = new Transaction(
                product.getNume(),
                product.getPret(),
                request.getBuyerEmail(),
                sellerEmail
        );

        transactionRepository.save(transaction);
        productRepository.deleteById(productId);

        return ResponseEntity.ok(new MessageResponse("Cumparare efectuata cu succes! Produsul a fost eliminat din vitrina."));
    }

    @GetMapping("/purchase/history")
    public ResponseEntity<List<Transaction>> getPurchaseHistory() {
        List<Transaction> history = transactionRepository.findAll();
        return ResponseEntity.ok(history);
    }
}