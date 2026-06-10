package com.upt.UniMarket.Dto;

import com.upt.UniMarket.Entity.Seller;
import com.upt.UniMarket.Entity.SellerStatus;

public class SellerView {
    private Long id;
    private String email;
    private String accountStatus;

    public SellerView(Seller seller)
    {
        this.id= seller.getId();
        this.email=seller.getEmail();
        SellerStatus s = seller.getAccountStatus();
        this.accountStatus = (s!=null?s.name() : SellerStatus.PENDING.name());
    }

    public Long getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    public String getAccountStatus()
    {
        return accountStatus;
    }
}
