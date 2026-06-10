package com.upt.UniMarket.Services;

import com.upt.UniMarket.Dto.OfferRequest;
import com.upt.UniMarket.Entity.Offer;
import com.upt.UniMarket.Entity.Product;
import com.upt.UniMarket.Repositories.OfferRepository;
import com.upt.UniMarket.Repositories.ProductRepository;
import com.upt.UniMarket.Repositories.TransactionRepository;
import com.upt.UniMarket.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Offer createOffer(OfferRequest request) throws Exception {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new Exception("Product not found"));

        if (!Boolean.TRUE.equals(product.getNegociabil())) {
            throw new Exception("This product is not negotiable");
        }

        if (product.getPretMin() != null && request.getProposedPrice() < product.getPretMin()) {
            throw new Exception("The proposed price is under the minimum margin");
        }

        Offer offer = new Offer(request.getProductId(), request.getBuyerId(), request.getProposedPrice());
        return offerRepository.save(offer);
    }

    public List<Offer> getOfferByProductId(Long prductId){
        return offerRepository.findByProductId(prductId);
    }

    @Transactional
    public void approveOffer(Long offerId) throws Exception {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new Exception("Offer not found"));

        Product product = productRepository.findById(offer.getProductId())
                .orElseThrow(() -> new Exception("Product not found"));

        String buyerEmail = userRepository.findById(offer.getBuyerId())
                .map(user -> user.getEmail())
                .orElse("unknown_buyer@email.com");

        String sellerEmail = userRepository.findById(product.getVanzatorId())
                .map(user -> user.getEmail())
                .orElse("unknown_seller@email.com");

        com.upt.UniMarket.Entity.Transaction transaction = new com.upt.UniMarket.Entity.Transaction(
                product.getNume(),
                offer.getProposedPrice().floatValue(),
                buyerEmail,
                sellerEmail
        );

        transactionRepository.save(transaction);

        offerRepository.deleteByProductId(offer.getProductId());
        productRepository.deleteById(offer.getProductId());
    }

}
