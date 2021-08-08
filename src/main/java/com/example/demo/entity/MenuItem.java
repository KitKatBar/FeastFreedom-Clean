package com.example.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="menus")
public class MenuItem implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	@NotNull
	@Size(min = 1, max = 50)
	private String itemName;

	@NotNull
	private boolean veg;
	
	@NotNull
	@Min(value = 0)
	private double price;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "kitchen_id")
	private Kitchen kitchen;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public boolean isVeg() {
		return veg;
	}

	public void setVeg(boolean veg) {
		this.veg = veg;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Kitchen getKitchen() {
		return kitchen;
	}

	public void setKitchen(Kitchen kitchen) {
		this.kitchen = kitchen;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) { return true; }
		if(!(o instanceof MenuItem)) { return false; }
		MenuItem m = (MenuItem) o;
		return itemName.equalsIgnoreCase(m.getItemName())
				&& veg == m.isVeg()
				&& Double.compare(price, m.getPrice()) == 0;
	}
	
	@Override
	public String toString() {
		return "Item Name: " + itemName + ", Veg: " + veg + ", Price: " + price;
	}
}
