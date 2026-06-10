package com.upt.UniMarket.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User{
    public Admin()
    {
        super();
    }

    public Admin(String email, String passwordHash)
    {
        super(email, passwordHash, "ADMIN");
    }
}
