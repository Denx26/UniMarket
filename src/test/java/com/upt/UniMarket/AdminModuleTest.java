package com.upt.UniMarket;

import com.upt.UniMarket.Controllers.AdminController;
import com.upt.UniMarket.Controllers.AuthController;
import com.upt.UniMarket.Dto.AuthRequest;
import com.upt.UniMarket.Dto.SellerView;
import com.upt.UniMarket.Entity.Seller;
import com.upt.UniMarket.Entity.SellerStatus;
import com.upt.UniMarket.Repositories.SellerRepository;
import com.upt.UniMarket.Repositories.UserRepository;
import com.upt.UniMarket.Services.AuthService;
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
public class AdminModuleTest {
    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AdminController adminController;

    @InjectMocks
    private AuthController authController;

    @Test
    void newSeller_startsAsPending() {
        Seller seller = new Seller("seller@test.com", "hash");
        assertEquals(SellerStatus.PENDING, seller.getAccountStatus());
    }

    @Test
    void addProduct_isBlocked_whenSellerIsNotActive() {
        Seller seller = new Seller("seller@test.com", "hash"); // PENDING
        assertFalse(seller.addProduct("Laptop", 1000));

        seller.setAccountStatus(SellerStatus.CANCELLED);
        assertFalse(seller.addProduct("Laptop", 1000));
    }

    @Test
    void addProduct_isAllowed_whenSellerIsActive() {
        Seller seller = new Seller("seller@test.com", "hash");
        seller.setAccountStatus(SellerStatus.ACTIVE);
        assertTrue(seller.addProduct("Laptop", 1000));
    }

    @Test
    void approveSeller_setsStatusToActive() {
        Seller seller = new Seller("seller@test.com", "hash");
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        ResponseEntity<?> response = adminController.approveSeller(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SellerStatus.ACTIVE, seller.getAccountStatus());
        verify(sellerRepository).save(seller);
    }

    @Test
    void rejectSeller_setsStatusToRejected() {
        Seller seller = new Seller("seller@test.com", "hash");
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        ResponseEntity<?> response = adminController.rejectSeller(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SellerStatus.REJECTED, seller.getAccountStatus());
        verify(sellerRepository).save(seller);
    }

    @Test
    void cancelSeller_setsStatusToCancelled() {
        Seller seller = new Seller("seller@test.com", "hash");
        seller.setAccountStatus(SellerStatus.ACTIVE);
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        ResponseEntity<?> response = adminController.cancelSeller(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SellerStatus.CANCELLED, seller.getAccountStatus());
        verify(sellerRepository).save(seller);
    }

    @Test
    void approveSeller_returns404_whenSellerDoesNotExist() {
        when(sellerRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = adminController.approveSeller(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sellerRepository, never()).save(any(Seller.class));
    }

    @Test
    void getPendingSellers_returnsOnlyPendingOnes() {
        Seller pending = new Seller("pending@test.com", "hash"); // PENDING
        Seller active = new Seller("active@test.com", "hash");
        active.setAccountStatus(SellerStatus.ACTIVE);
        when(sellerRepository.findAll()).thenReturn(List.of(pending, active));

        ResponseEntity<List<SellerView>> response = adminController.getPendingSellers();

        assertEquals(1, response.getBody().size());
        assertEquals("pending@test.com", response.getBody().get(0).getEmail());
    }

    @Test
    void cancelledSeller_cannotLogIn() {
        Seller seller = new Seller("seller@test.com", "hash");
        seller.setAccountStatus(SellerStatus.CANCELLED);

        AuthRequest request = new AuthRequest();
        request.setEmail("seller@test.com");
        request.setPassword("123");

        when(userRepository.findByEmail("seller@test.com")).thenReturn(Optional.of(seller));
        when(authService.comparePassword("123", "hash")).thenReturn(true);

        ResponseEntity<?> response = authController.loginUser(request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
