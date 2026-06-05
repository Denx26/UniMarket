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

    public void addProduct(){

    }

    public void cancelSale(){

    }


}
