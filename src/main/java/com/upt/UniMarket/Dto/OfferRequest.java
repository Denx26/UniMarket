package com.upt.UniMarket.Dto;

public class OfferRequest {
    private Long productId;
    private Long buyerId;
    private Double proposedPrice;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Double getProposedPrice() { return proposedPrice; }
    public void setProposedPrice(Double proposedPrice) { this.proposedPrice = proposedPrice; }
}