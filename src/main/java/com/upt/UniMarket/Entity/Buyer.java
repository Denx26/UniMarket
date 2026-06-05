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

    public void placeOrder(String productName, double itemPrice){
        System.out.println("User Email: " + this.getEmail());
        System.out.println("Action: Placed a new order for: [" + productName + "] valued at $" + itemPrice);
    }

    public void viewHistory(){
        System.out.println("User Email: " + this.getEmail());
        System.out.println("Action: Retrieved order records history from SQLite for user ID: " + this.getId());
    }
}
