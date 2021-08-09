package com.example.demo.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.KitchenDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Kitchen;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repo.KitchenRepo;
import com.example.demo.repo.UserRepo;

@Service
@Transactional
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private KitchenRepo kRepo;
	
	@Autowired
	private UserRepo uRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public Kitchen saveKitchen(KitchenDto registration) {
		Kitchen kitchen = new Kitchen();
		kitchen.setName(registration.getName());
		kitchen.setEmail(registration.getEmail());
        //user.setPassword(registration.getPassword());
        //user.setConfirmPassword(registration.getConfirmPassword());
		kitchen.setPassword(passwordEncoder.encode(registration.getPassword()));
        //user.setConfirmPassword(user.getPassword());
        //user.setConfirmPassword(passwordEncoder.encode(registration.getConfirmPassword()));
		kitchen.setRoles(Arrays.asList(new Role("ROLE_KITCHEN")));
        return kRepo.save(kitchen);
    }
	
	public User saveUser(UserDto registration) {
		User user = new User();
        user.setName(registration.getFirstName() + " " + registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPhone(registration.getPhone());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return uRepo.save(user);
    }
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//System.out.println("loading kitchen");
		//System.out.println("email is: " + email);
		Kitchen kitchen = kRepo.findByEmail(email);
		User user = uRepo.findByEmail(email);
		
		if ((kitchen == null) & (user == null)) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		/*
		if (kitchen == null ) {
			User user = uRepo.findByEmail(email);
			if (user == null) {
				//System.out.println("null?");
				throw new UsernameNotFoundException("Invalid username or password.");
			}
			//System.out.println("permission user");
			return new org.springframework.security.core.userdetails.User(user.getEmail(),
					user.getPassword(),
					mapRolesToAuthorities(user.getRoles()));
		}
		//System.out.println("permission kitchen");
		return new org.springframework.security.core.userdetails.User(kitchen.getEmail(),
				kitchen.getPassword(),
				mapRolesToAuthorities(kitchen.getRoles()));
				*/
		else if ((kitchen != null) && (user != null)) {
			try {
				return new org.springframework.security.core.userdetails.User(kitchen.getEmail(),
						kitchen.getPassword(),
						mapRolesToAuthorities(kitchen.getRoles()));	
			} catch (UsernameNotFoundException e) {
				return new org.springframework.security.core.userdetails.User(user.getEmail(),
						user.getPassword(),
						mapRolesToAuthorities(user.getRoles()));
			}
			//throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		else if ((kitchen != null) && (user == null)) {
			return new org.springframework.security.core.userdetails.User(kitchen.getEmail(),
					kitchen.getPassword(),
					mapRolesToAuthorities(kitchen.getRoles()));
		}
		
		else if ((user != null) && (kitchen == null)) {
			return new org.springframework.security.core.userdetails.User(user.getEmail(),
					user.getPassword(),
					mapRolesToAuthorities(user.getRoles()));
		}
		
		else {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}
}
