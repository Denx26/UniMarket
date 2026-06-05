package com.upt.UniMarket.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUYER")
public class Buyer extends User{

    public Buyer() {
        super();
    }

    public Buyer(String email, String passwordHash) {
        super(email, passwordHash, "BUYER");
    }

    public void placeOrder(){

    }

    public void viewHistory(){

    }
}
