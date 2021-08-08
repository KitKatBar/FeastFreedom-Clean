package com.example.demo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="kitchens")
public class Kitchen implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String email;
	private String password;
	
	private String [] daysOpen;
	private int startTime;
	private int endTime;

	@OneToMany(mappedBy = "kitchen", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuItem> menu;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "kitchen_roles",
            joinColumns = @JoinColumn(
                    name = "k_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "r_id", referencedColumnName = "id"))
    private Collection<Role> roles;

	public Kitchen() {
		menu = new ArrayList<MenuItem>();
	}
	
	public Kitchen(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		menu = new ArrayList<MenuItem>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String[] getDaysOpen() {
	      return daysOpen;
	}
	
	public void setDaysOpen(String[] daysOpen) {
		this.daysOpen = daysOpen;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
    
	public List<MenuItem> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuItem> menu) {
		this.menu = menu;
	}
	
	public void addMenuItem(MenuItem item) {
    	if (item != null) {
    		if (menu == null)
    			menu = new ArrayList<MenuItem>();
    		item.setKitchen(this);
    		menu.add(item);
    	}
    }
    
    public void deleteMenuItem(MenuItem item) {
    	menu.remove(item);
    	item.setKitchen(null);
    }
	
	public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
	
	@Override
	public boolean equals(Object o) {
		if(o == this) { return true; }
		if(!(o instanceof Kitchen)) { return false; }
		Kitchen k = (Kitchen) o;

		return name.equalsIgnoreCase(k.getName())
			&& email.equalsIgnoreCase(k.getEmail())
			&& password.equalsIgnoreCase(k.getPassword());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + name + ", Email: " + email + ", Menu: [");
		for (int i = 0; i < menu.size(); i++) {
			sb.append("Item Name: " + menu.get(i).getItemName() + ", ");
			sb.append("Veg: " + menu.get(i).isVeg() + ", ");
			sb.append("Price: " + menu.get(i).getPrice() + "]");
		}
		
		return sb.toString();
	}
}
