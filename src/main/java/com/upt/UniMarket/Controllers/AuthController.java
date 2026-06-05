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
    private AuthService authService; // Inject AuthService

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Email is already registered!"));
        }

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

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !authService.comparePassword(request.getPassword(), userOpt.get().getPasswordHash())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid email or password."));
        }

        User user = userOpt.get();

        String token = authService.generateSessionToken(user);
        System.out.println("Generated active session token: " + token);

        return ResponseEntity.ok(new AuthResponse(user.getEmail(), user.getRole(), user.getPasswordHash()));
    }
}
