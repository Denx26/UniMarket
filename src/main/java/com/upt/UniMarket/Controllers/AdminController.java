package com.upt.UniMarket.Controllers;

import com.upt.UniMarket.Dto.MessageResponse;
import com.upt.UniMarket.Dto.SellerView;
import com.upt.UniMarket.Entity.Seller;
import com.upt.UniMarket.Entity.SellerStatus;
import com.upt.UniMarket.Repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private SellerRepository sellerRepository;

    @GetMapping("/sellers")
    public ResponseEntity<List<SellerView>> getAllSellers()
    {
        List<SellerView> sellers = sellerRepository.findAll()
                .stream().map(SellerView::new).toList();

        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/sellers/pending")
    public ResponseEntity<List<SellerView>> getPendingSellers()
    {
        List<SellerView> pending = sellerRepository.findAll()
                .stream().filter(s->s.getAccountStatus()== SellerStatus.PENDING)
                .map(SellerView::new).toList();
        return ResponseEntity.ok(pending);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveSeller(@PathVariable Long id)
    {
        return changeStatus(id, SellerStatus.ACTIVE, "approved");
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectSeller(@PathVariable Long id)
    {
        return changeStatus(id, SellerStatus.REJECTED, "rejected");
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelSeller(@PathVariable Long id)
    {
        return changeStatus(id, SellerStatus.CANCELLED, "cancelled");
    }

    private ResponseEntity<?> changeStatus(Long id, SellerStatus newStatus, String actionLabel)
    {
        Optional<Seller> sellerOpt = sellerRepository.findById(id);

        if(sellerOpt.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Seller with id "+id+" does not exist"));
        }

        Seller seller = sellerOpt.get();
        seller.setAccountStatus(newStatus);
        sellerRepository.save(seller);

        return ResponseEntity.ok(new MessageResponse("Seller "+seller.getEmail()+" has been "+actionLabel));
    }
}
