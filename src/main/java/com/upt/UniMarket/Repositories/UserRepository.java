package com.upt.UniMarket.Repositories;

import com.upt.UniMarket.Entity.Buyer;
import com.upt.UniMarket.Entity.Seller;
import com.upt.UniMarket.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Buyer> findBuyerByEmail(String email);
    Optional<Seller> findSellerByEmail(String email);
}
