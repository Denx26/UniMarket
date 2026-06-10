package com.upt.UniMarket.Entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SELLER")
public class Seller extends User{

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private SellerStatus accountStatus = SellerStatus.PENDING;

    public Seller() {
        super();
    }

    public Seller(String email, String passwordHash) {
        super(email, passwordHash, "SELLER");
        this.accountStatus = SellerStatus.PENDING;
    }

    public SellerStatus getAccountStatus()
    {
        return accountStatus;
    }

    public void setAccountStatus(SellerStatus accountStatus)
    {
        this.accountStatus=accountStatus;
    }

    public boolean addProduct(String productName, double productPrice){
        System.out.println("Start Executing addProduct");
        if(this.accountStatus != SellerStatus.ACTIVE){
            System.out.println("DENIED: Seller " + this.getEmail() + " is NOT active. Action blocked!");
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
