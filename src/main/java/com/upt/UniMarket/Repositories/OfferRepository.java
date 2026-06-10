package com.upt.UniMarket.Repositories;

import com.upt.UniMarket.Entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByProductId(Long productId);

    @Transactional
    void deleteByProductId(Long productId);
}