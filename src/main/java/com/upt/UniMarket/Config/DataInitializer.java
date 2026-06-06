package com.upt.UniMarket.Config;

import com.upt.UniMarket.Entity.Admin;
import com.upt.UniMarket.Repositories.UserRepository;
import com.upt.UniMarket.Services.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthService authService;

    public DataInitializer(UserRepository userRepository, AuthService authService)
    {
        this.userRepository=userRepository;
        this.authService=authService;
    }

    @Override
    public void run(String ... args)
    {
        String adminEmail = "admin@email.com";

        if(!userRepository.existsByEmail(adminEmail))
        {
            String hashedPassword = authService.hashPassword("admin");
            Admin admin = new Admin(adminEmail, hashedPassword);
            userRepository.save(admin);
            System.out.println("Default ADMIN account created: " + adminEmail +" / admin");
        }
        else
        {
            System.out.println("ADMIN account already exists");
        }
    }
}
