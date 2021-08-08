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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepo;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepo uRepo;
	
	//@Autowired
	//@Lazy
    //private BCryptPasswordEncoder userpasswordEncoder;
	
	public User findByEmail(String email) {
        return uRepo.findByEmail(email);
    }
	
	public User findUser(long id) {
		return uRepo.findById(id).get();
	}
	
	public List<User> findUserList() {
		return uRepo.findAll();
	}
	
	public void saveUser(User user) {
		uRepo.save(user);
	}
	
	public void deleteUser(long id) {
		uRepo.deleteById(id);
	}
	
	@Transactional
	public void addToCart(User user, CartItem item) {
		item.setUser(user);
		uRepo.findById(user.getId()).get().getCart().add(item);
		user.setCost(user.getCost() + item.getPrice());
	}
	
	public List<CartItem> getCart(User user) {
		return uRepo.findById(user.getId()).get().getCart();
	}
	
	@Transactional
	public void deleteCartItem(User user, Long id) {
		for (int i = 0; i < user.getCart().size(); i++) {
			if (user.getCart().get(i).getId().equals(id)) {
				System.out.println("i'm in if statement");
				user.deleteCartItem(user.getCart().get(i));
				//k.getMenu().remove(i);
				System.out.println("size after remove: " + user.getCart().size());
				System.out.println("i removed item");
				uRepo.save(user);
				System.out.println("i updated repo");
				break;
			}
		}
		
	}
	
	/*
	public User save(UserDto registration) {
		User user = new User();
        user.setName(registration.getName());
        user.setEmail(registration.getEmail());
        user.setPhone(registration.getPhone());
        user.setPassword(userpasswordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return repo.save(user);
    }
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("loading user");
        User user = repo.findByEmail(email);
        if(user ==null)
        	throw new UsernameNotFoundException("Invalid username or password.");
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}*/
}
