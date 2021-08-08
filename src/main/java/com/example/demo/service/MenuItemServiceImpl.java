package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Kitchen;
import com.example.demo.entity.MenuItem;
import com.example.demo.repo.KitchenRepo;

@Service
public class MenuItemServiceImpl {

	@Autowired
	KitchenRepo kRepo;
	
	public List<MenuItem> findMenu(Kitchen kitchen) {
		return kRepo.findById(kitchen.getId()).get().getMenu();
	}
	
	public MenuItem findMenuItem(Kitchen kitchen, Long id) {
		Kitchen k = kRepo.findById(kitchen.getId()).get();
		for (int i = 0; i < k.getMenu().size(); i++) {
			if (k.getMenu().get(i).getId().equals(id))
				return k.getMenu().get(i);
		}
		
		return null;
	}
	
	@Transactional
	public void saveMenuItem(Kitchen k) {
		kRepo.save(k);
	}
	
	@Transactional
	public void editMenuItem(Kitchen k, MenuItem m) {
		//Kitchen k = kRepo.findById(id).get();
		//System.out.println("menu id: " + m.getId());
		System.out.println("menu contents: " + m.toString());
		for (int i = 0; i < k.getMenu().size(); i++) {
			if (k.getMenu().get(i).getId().equals(m.getId())) {
				k.getMenu().get(i).setItemName(m.getItemName());
				k.getMenu().get(i).setVeg(m.isVeg());
				k.getMenu().get(i).setPrice(m.getPrice());
				break;
			}
		}
		//kRepo.findById(id).get().getMenu().add(m);
		kRepo.save(k);
	}
	
	@Transactional
	public void deleteMenuItem(Kitchen k, Long m_id) {
		//System.out.println("check kitchen ID: " + k_id);
		//Kitchen k = kRepo.getById(k_id);
		System.out.println("i made it here");
		System.out.println("check kitchen ID: " + k.getId());
		System.out.println("size is: " + k.getMenu().size());
		System.out.println("id is: " + k.getMenu().get(0).getId());
		System.out.println("name is: " + k.getMenu().get(0).getItemName());
		System.out.println("veg is: " + k.getMenu().get(0).isVeg());
		System.out.println("price is: " + k.getMenu().get(0).getPrice());
		for (int i = 0; i < k.getMenu().size(); i++) {
			if (k.getMenu().get(i).getId().equals(m_id)) {
				System.out.println("i'm in if statement");
				k.deleteMenuItem(k.getMenu().get(i));
				//k.getMenu().remove(i);
				System.out.println("size after remove: " + k.getMenu().size());
				System.out.println("i removed item");
				kRepo.save(k);
				System.out.println("i updated repo");
				break;
			}
		}
		//System.out.println("i leave for loop");
	}
	
	public CartItem convert(MenuItem item) {
		CartItem converted = new CartItem();
		converted.setItemName(item.getItemName());
		converted.setVeg(item.isVeg());
		converted.setPrice(item.getPrice());
		return converted;
	}
}
