package com.upt.UniMarket.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private Float finalPrice;
    private String buyerEmail;
    private String sellerEmail;

    public Transaction() {}

    public Transaction(String productName, Float finalPrice, String buyerEmail, String sellerEmail) {
        this.productName = productName;
        this.finalPrice = finalPrice;
        this.buyerEmail = buyerEmail;
        this.sellerEmail = sellerEmail;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Float getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Float finalPrice) { this.finalPrice = finalPrice; }

    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }
}