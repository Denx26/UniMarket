package com.upt.UniMarket;

import com.upt.UniMarket.Controllers.PurchaseController;
import com.upt.UniMarket.Dto.PurchaseRequest;
import com.upt.UniMarket.Entity.Buyer;
import com.upt.UniMarket.Entity.Product;
import com.upt.UniMarket.Entity.Transaction;
import com.upt.UniMarket.Entity.User;
import com.upt.UniMarket.Repositories.ProductRepository;
import com.upt.UniMarket.Repositories.TransactionRepository;
import com.upt.UniMarket.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseModuleTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PurchaseController purchaseController;

    private Product makeProd(Long pid, Long vId, String nume, Float pret) {
        Product p = new Product();
        p.setPid(pid);
        p.setVanzatorId(vId);
        p.setNume(nume);
        p.setPret(pret);
        return p;
    }

    private PurchaseRequest makeReq(String email) {
        PurchaseRequest req = new PurchaseRequest();
        req.setBuyerEmail(email);
        return req;
    }

    private User makeUser(String email) {
        Buyer u = new Buyer();
        u.setEmail(email);
        return u;
    }

    @Test
    void getAvailableProducts_returnsAll() {
        List<Product> list = List.of(
                makeProd(1L, 2L, "Laptop", 2500.0f),
                makeProd(2L, 3L, "Mouse", 100.0f)
        );
        when(productRepository.findAll()).thenReturn(list);

        ResponseEntity<List<Product>> res = purchaseController.getAvailableProducts();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(2, res.getBody().size());
    }

    @Test
    void purchaseProduct_ifFound() {
        Product p = makeProd(10L, 99L, "Tastatura", 150.0f);
        User s = makeUser("vanzator@email.com");
        PurchaseRequest req = makeReq("cumparator@email.com");

        when(productRepository.findById(10L)).thenReturn(Optional.of(p));
        when(userRepository.findById(99L)).thenReturn(Optional.of(s));

        ResponseEntity<?> res = purchaseController.purchaseProduct(10L, req);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(productRepository, times(1)).deleteById(10L);
    }

    @Test
    void purchaseProduct_notFound() {
        PurchaseRequest req = makeReq("cumparator@email.com");

        when(productRepository.findById(5L)).thenReturn(Optional.empty());

        ResponseEntity<?> res = purchaseController.purchaseProduct(5L, req);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        verify(transactionRepository, never()).save(any());
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void purchaseProduct_missingSeller() {
        Product p = makeProd(12L, 88L, "Monitor", 800.0f);
        PurchaseRequest req = makeReq("cumparator@email.com");

        when(productRepository.findById(12L)).thenReturn(Optional.of(p));
        when(userRepository.findById(88L)).thenReturn(Optional.empty());

        ResponseEntity<?> res = purchaseController.purchaseProduct(12L, req);
        assertEquals(HttpStatus.OK, res.getStatusCode());

        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void getPurchaseHistory_returnsAll() {
        List<Transaction> list = List.of(
                new Transaction("Laptop", 2500.0f, "b1@email.com", "s@email.com"),
                new Transaction("Mouse", 100.0f, "b2@email.com", "s@email.com")
        );
        when(transactionRepository.findAll()).thenReturn(list);

        ResponseEntity<List<Transaction>> res = purchaseController.getPurchaseHistory();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(2, res.getBody().size());
    }
}