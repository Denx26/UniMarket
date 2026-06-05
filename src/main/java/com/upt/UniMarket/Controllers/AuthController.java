package com.upt.UniMarket.Controllers;

import com.upt.UniMarket.Entity.*;
import com.upt.UniMarket.Dto.*;
import com.upt.UniMarket.Repositories.UserRepository;
import com.upt.UniMarket.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    // --- REGISTRATION LOGIC ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Email is already registered!"));
        }

        // Encrypt the password using AuthService before saving to SQLite
        String securePasswordHash = authService.hashPassword(request.getPassword());

        User newUser;
        String requestedRole = request.getRole() != null ? request.getRole().toUpperCase() : "BUYER";

        if ("SELLER".equals(requestedRole)) {
            newUser = new Seller(request.getEmail(), securePasswordHash);
        } else {
            newUser = new Buyer(request.getEmail(), securePasswordHash);
        }

        userRepository.save(newUser);
        return ResponseEntity.ok(new MessageResponse("Registration successful!"));
    }

    // --- LOGIN LOGIC (Matches Activity Diagram Exactly) ---
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest request) {
        // 1. Caută User în SQLite (DatabaseCheck)
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        // [DIAGRAM]: Email inexistent -> UserNotFound -> Afișează "Eroare Login"
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("User account does not exist."));
        }

        User user = userOpt.get();

        // 2. PasswordVerification & BcryptCheck
        boolean isPasswordCorrect = authService.comparePassword(request.getPassword(), user.getPasswordHash());

        // [DIAGRAM]: Parola greșită -> InvalidPassword -> Afișează "Eroare Login"
        if (!isPasswordCorrect) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid password."));
        }

        // 3. [DIAGRAM]: Parola corectă -> Success -> Generează JWT / Sesiune (SessionCreated)
        String sessionToken = authService.generateSessionToken(user);

        // 4. Send fields back to client for Role-Based Redirection
        return ResponseEntity.ok(new AuthResponse(user.getEmail(), user.getRole(), sessionToken));
    }
}
