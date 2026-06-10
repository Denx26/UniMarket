package com.upt.UniMarket.Controllers;


import com.upt.UniMarket.Dto.OfferRequest;
import com.upt.UniMarket.Entity.Offer;
import com.upt.UniMarket.Services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @PostMapping
    public ResponseEntity<?> createOffer(@RequestBody OfferRequest request){
        try{
            Offer savedOffer = offerService.createOffer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOffer);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Offer>> getOffers(@PathVariable Long productId) {
        return ResponseEntity.ok(offerService.getOfferByProductId(productId));
    }

    @PostMapping("/{offerId}/approve")
    public ResponseEntity<?> approveOffer(@PathVariable() Long offerId) {
        try {
            offerService.approveOffer(offerId);
            return ResponseEntity.ok("Offer approved successfully, product has been sold");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
