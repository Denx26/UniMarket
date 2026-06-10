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

	public Long getPid() {
		return pid;
	}

	public Long getVanzatorId() {
		return vanzatorId;
	}

	public void setVanzatorId(Long vanzatorId) {
		this.vanzatorId = vanzatorId;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public Float getPret() {
		return pret;
	}

	public void setPret(Float pret) {
		this.pret = pret;
	}

	public String getDescriere() {
		return descriere;
	}

	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}

	public Boolean getNegociabil() {
		return negociabil;
	}

	public void setNegociabil(Boolean negociabil) {
		this.negociabil = negociabil;
	}

	public Float getPretMin() {
		return pretMin;
	}

	public void setPretMin(Float pretMin) {
		this.pretMin = pretMin;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}
}
