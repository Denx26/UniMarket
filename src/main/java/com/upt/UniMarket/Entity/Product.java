package com.upt.UniMarket.Entity;

import java.util.Optional;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pid;
	private Long vanzatorId;
	private String nume;
	private Float pret;
	private String descriere;
	private Boolean negociabil;
	@Column(nullable = true)
	private Float pretMin;

	public boolean pretValid(float pret) {
		return pretMin != null && pret > pretMin || pret > this.pret;
	}

	public void setNegociabil(boolean negociabil) {
		this.negociabil = negociabil;
	}

	public boolean getNegeciabil() {
		return negociabil;
	}
}
