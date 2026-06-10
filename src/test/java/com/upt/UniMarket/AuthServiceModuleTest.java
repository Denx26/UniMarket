package com.upt.UniMarket;

import com.upt.UniMarket.Services.AuthService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    void hashPassword_returnsSHA256Hash() {
        String hash = authService.hashPassword("test123");
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void comparePassword_correctPassword_returnsTrue() {
        String hash = authService.hashPassword("mypassword");
        assertTrue(authService.comparePassword("mypassword", hash));
    }

    @Test
    void comparePassword_wrongPassword_returnsFalse() {
        String hash = authService.hashPassword("mypassword");
        assertFalse(authService.comparePassword("wrongpassword", hash));
    }

    @Test
    void generateSessionToken_returnsNonNullToken() {
        com.upt.UniMarket.Entity.Buyer buyer = new com.upt.UniMarket.Entity.Buyer("test@test.com", "hash");
        String token = authService.generateSessionToken(buyer);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
}