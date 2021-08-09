package com.example.demo.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.entity.Kitchen;

public interface KitchenService extends UserDetailsService {

	public Kitchen findByEmail(String email);
	public Kitchen findKitchen(Long id);
	public List<Kitchen> findKitchenList();
	public void saveKitchen(Kitchen k);
	public void deleteKitchen(Long id);
	public Long getKitchenId(Kitchen k);
	//public Kitchen save(KitchenDto registration);
}
