package com.example.demo.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Kitchen;
import com.example.demo.entity.Role;
import com.example.demo.repo.KitchenRepo;

@Service
public class KitchenServiceImpl implements KitchenService {

	@Autowired
	KitchenRepo kRepo;
	
	//@Autowired
	//@Lazy
    //private BCryptPasswordEncoder passwordEncoder;
	
	public Kitchen findByEmail(String email) {
        return kRepo.findByEmail(email);
    }
	
	public Kitchen findKitchen(Long id) {
		return kRepo.findById(id).get();
	}
	
	public List<Kitchen> findKitchenList() {
		return kRepo.findAll();
	}
	
	public void saveKitchen(Kitchen k) {
		kRepo.save(k);
	}
	
	public void deleteKitchen(Long id) {
		kRepo.deleteById(id);
	}
	
	public Long getKitchenId(Kitchen k) {
		List<Kitchen> list = kRepo.findAll();
		//System.out.println("Name: " + k.getName());
		//System.out.println("Email: " + k.getEmail());
		//System.out.println("Password: " + k.getPassword());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(k)) {
				//System.out.println("id is: " + list.get(i).getId());
				return list.get(i).getId();
			}
		}
		
		return null;
	}
	
	public String getKitchenEmail(Kitchen k) {
		List<Kitchen> list = kRepo.findAll();
		//System.out.println("Name: " + k.getName());
		//System.out.println("Email: " + k.getEmail());
		//System.out.println("Password: " + k.getPassword());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(k)) {
				//System.out.println("id is: " + list.get(i).getId());
				return list.get(i).getEmail();
			}
		}
		
		return null;
	}
	
	public String getKitchenName(Kitchen k) {
		List<Kitchen> list = kRepo.findAll();
		//System.out.println("Name: " + k.getName());
		//System.out.println("Email: " + k.getEmail());
		//System.out.println("Password: " + k.getPassword());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(k)) {
				//System.out.println("id is: " + list.get(i).getId());
				return list.get(i).getName();
			}
		}
		
		return null;
	}
	
	/*
	public Kitchen save(KitchenDto registration) {
		Kitchen user = new Kitchen();
        user.setName(registration.getName());
        user.setEmail(registration.getEmail());
        //user.setPassword(registration.getPassword());
        //user.setConfirmPassword(registration.getConfirmPassword());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        //user.setConfirmPassword(user.getPassword());
        //user.setConfirmPassword(passwordEncoder.encode(registration.getConfirmPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_KITCHEN")));
        return kRepo.save(user);
    }
	*/
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("loading kitchen");
		System.out.println("email is: " + email);
		Kitchen kitchen = kRepo.findByEmail(email);
		if (kitchen == null){
			System.out.println("null?");
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(kitchen.getEmail(),
				kitchen.getPassword(),
				mapRolesToAuthorities(kitchen.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}
}
