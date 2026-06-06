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

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("User account does not exist."));
        }

        User user = userOpt.get();

        boolean isPasswordCorrect = authService.comparePassword(request.getPassword(), user.getPasswordHash());

        if (!isPasswordCorrect) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid password."));
        }

        if(user instanceof Seller seller && seller.getAccountStatus() == SellerStatus.CANCELLED)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("This seller account has been cancelled by the administrator"));
        }

        String sessionToken = authService.generateSessionToken(user);

        return ResponseEntity.ok(new AuthResponse(user.getId(), user.getEmail(), user.getRole(), sessionToken));
    }
}
