package com.upt.UniMarket.Dto;

public class PurchaseRequest {
    private String buyerEmail;

    public PurchaseRequest() {}

    public PurchaseRequest(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }
}