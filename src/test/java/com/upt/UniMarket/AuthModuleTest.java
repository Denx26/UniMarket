package com.upt.UniMarket;

import com.upt.UniMarket.Controllers.AuthController;
import com.upt.UniMarket.Dto.AuthRequest;
import com.upt.UniMarket.Entity.Buyer;
import com.upt.UniMarket.Repositories.UserRepository;
import com.upt.UniMarket.Services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_emailAlreadyExists_returnsBadRequest() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("pass");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void register_newEmail_returnsOk() {
        AuthRequest request = new AuthRequest();
        request.setEmail("new@test.com");
        request.setPassword("pass");
        request.setRole("BUYER");

        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(authService.hashPassword("pass")).thenReturn("hashedpass");

        ResponseEntity<?> response = authController.registerUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void login_userNotFound_returnsNotFound() {
        AuthRequest request = new AuthRequest();
        request.setEmail("ghost@test.com");
        request.setPassword("pass");

        when(userRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.loginUser(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void login_wrongPassword_returnsUnauthorized() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("wrongpass");

        Buyer buyer = new Buyer("test@test.com", "hashedpass");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(buyer));
        when(authService.comparePassword("wrongpass", "hashedpass")).thenReturn(false);

        ResponseEntity<?> response = authController.loginUser(request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void login_correctCredentials_returnsOk() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("correctpass");

        Buyer buyer = new Buyer("test@test.com", "hashedpass");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(buyer));
        when(authService.comparePassword("correctpass", "hashedpass")).thenReturn(true);
        when(authService.generateSessionToken(buyer)).thenReturn("token-uuid");

        ResponseEntity<?> response = authController.loginUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}