package com.upt.UniMarket.Services;

import com.upt.UniMarket.Dto.OfferRequest;
import com.upt.UniMarket.Entity.Offer;
import com.upt.UniMarket.Entity.Product;
import com.upt.UniMarket.Repositories.OfferRepository;
import com.upt.UniMarket.Repositories.ProductRepository;
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
        Offer offer = offerRepository.findById(offerId).orElseThrow(()->new Exception("Offer not found"));

        // partea cu sales history

        offerRepository.deleteByProductId(offer.getProductId());

        productRepository.deleteById(offer.getProductId());
    }

}
