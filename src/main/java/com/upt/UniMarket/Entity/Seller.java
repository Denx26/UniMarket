package com.upt.UniMarket.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SELLER")
public class Seller extends User{

    @Column(name="isApproved", nullable = false)
    private boolean isApproved;

    public Seller() {
        super();
    }

    public Seller(String email, String passwordHash) {
        super(email, passwordHash, "SELLER");
        this.isApproved = false;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean addProduct(String productName, double productPrice){
        System.out.println("Start Executing addProduct");
        if(!this.isApproved){
            System.out.println("DENIED: Seller " + this.getEmail() + " is NOT approved. Action blocked!");
            return false;
        }
        System.out.println("SUCCESS: Seller " + this.getEmail() + " added [" + productName + "] at $" + productPrice);
        return true;
    }

    public void cancelSale(Long transactionId){
        System.out.println("Seller Email: " + this.getEmail());
        System.out.println("Action: Requesting complete void of active deal reference item: #" + transactionId);
    }


}
