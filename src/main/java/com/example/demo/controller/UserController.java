package com.example.demo.controller;


import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Kitchen;
import com.example.demo.entity.MenuItem;
import com.example.demo.entity.User;
import com.example.demo.service.KitchenServiceImpl;
import com.example.demo.service.MenuItemServiceImpl;
import com.example.demo.service.UserService;

@Controller
@SessionAttributes({"user", "kitchen"})
public class UserController {

	@Autowired
	private UserService uService;
	
	@Autowired
	private KitchenServiceImpl kService;
	
	@Autowired
	private MenuItemServiceImpl mService; 
	
	@ModelAttribute("kitchen")
	public Kitchen kitchen() {
		return new Kitchen();
	}
	
	@GetMapping("/user_homepage")
	public String userHome(Principal principal, Model model) {
		String email = principal.getName();
		User user = new User();
		List<User> list = uService.findUserList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getEmail().equals(email)) {
				user = list.get(i);
				model.addAttribute("user", user);
				break;
			}
		}
		
		return "user_homepage";
	}
	
	@GetMapping("/user_order_kitchen")
	public String viewKitchens(Model model) {
		//User u = (User) model.getAttribute("user");
		//System.out.println("check u_id order kitchen: " + u.getId());
		List<Kitchen> list = kService.findKitchenList();
		model.addAttribute("kitchens", list);
		//List<MenuItem> listProducts = proservice();
		//model.addAttribute("listProducts", listProducts);
		//List<CartItem> list = userservice.getCart(user);
		//user.setCart(list);
		//model.addAttribute("user", user);
		//User user = userservice.get(1);
		//model.addAttribute("user", user);
		return "user_view_kitchen";
	}
	
	@RequestMapping("/user_order_kitchen/{id}")
	public String selectKitchen(@PathVariable(name = "id") Long id, Model model,
			RedirectAttributes redirectAttributes) {
		//User u = (User) model.getAttribute("user");
		Kitchen k = kService.findKitchen(id);
		redirectAttributes.addFlashAttribute("kitchen", k);
		//System.out.println("check u_id order kitchen: " + u.getId());
		System.out.println("k_id before pass: " + k.getId());
		return "redirect:/user_order_menu";
	}
	
	@GetMapping("/user_order_menu")
	public String viewMenu(@ModelAttribute("kitchen") Kitchen kitchen, Model model,
			RedirectAttributes redirectAttributes) {
		//System.out.println("k_id of model: " + k.getId());
		//User u = (User) model.getAttribute("user");
		//Kitchen kitchen = (Kitchen) redirectAttributes.getFlashAttributes();
		//Kitchen k = kService.findKitchen(kitchen.getId());
		System.out.println("k_id after pass: " + kitchen.getId());
		//System.out.println("check u_id order kitchen: " + u.getId());
		List<MenuItem> list = mService.findMenu(kitchen);
		kitchen.setMenu(list);
		//model.addAttribute("kitchen", kitchen);
		//model.addAttribute("kitchen", k);
		//redirectAttributes.addFlashAttribute("kitchen", kitchen);
		model.addAttribute("items", list);
		//List<MenuItem> listProducts = proservice();
		//model.addAttribute("listProducts", listProducts);
		//List<CartItem> list = userservice.getCart(user);
		//user.setCart(list);
		//model.addAttribute("user", user);
		//User user = userservice.get(1);
		//model.addAttribute("user", user);
		return "user_view_menu";
	}
	
	@RequestMapping("/user_confirm")
	public String confirmOrder(Model model) {
		User u = (User) model.getAttribute("user");
		//User user = userservice.get(1);
		//model.addAttribute("user", user);
		System.out.println(u);
		//List<ResUser> listUsers = service.listAll();
		//model.addAttribute("listUsers", listUsers);
		//List<Product> listProducts = proservice.listAll();
	//	model.addAttribute("listProducts", listProducts);
		//model.addAllAttributes("user", user);
		return "user_confirm_order";
	}
	
	@GetMapping("/user_view_cart")
	public String viewCart(Model model) {
		User u = (User) model.getAttribute("user");
		//User user = userservice.get(1);
		//model.addAttribute("user", user);
		//System.out.println("cart size: " + user.getCart().size());
		//System.out.println(user);
		//List<ResUser> listUsers = service.listAll();
		//model.addAttribute("listUsers", listUsers);
		//List<CartItem> list = userservice.getCart(user);
		//user.setCart(list);
		//model.addAttribute("items", list);
		//model.addAttribute("products", list);
		//model.addAllAttributes("user", user);
		List<CartItem> list = uService.getCart(u);
		u.setCart(list);
		model.addAttribute("items", list);
		return "user_view_cart";
	}
	
	@RequestMapping("/add_cart_item/{id}")
	public String addCartItem(@PathVariable(name = "id") Long id,
			Model model, RedirectAttributes redirectAttributes) {
		User u = (User) model.getAttribute("user");
		Kitchen k = (Kitchen) model.getAttribute("kitchen");
		System.out.println("k_id after pass: " + k.getId());
		System.out.println("check u_id order kitchen: " + u.getId());
		MenuItem item = mService.findMenuItem(k, id);
		//model.addAttribute("product", product);	
		//User user = userservice.get(1);
		//model.addAttribute("user", user);
		System.out.println("user id: " + u.getId());
		System.out.println("user name:" + u.getName());
		System.out.println("before conversion");
		CartItem converted = mService.convert(item);
		//System.out.println("item info: " + item.getName() + item.getBrand() + item.getMadein() + item.getPrice());
		System.out.println("after conversion");
		//converted.setUser(user);
		System.out.println("before add");
		//user.addItem(converted);
		uService.addToCart(u, converted);
		System.out.println("added product");
		redirectAttributes.addFlashAttribute("kitchen", k);
		uService.saveUser(u);
		
		//List<ResUser> listUsers = service.listAll();
		//model.addAttribute("listUsers", listUsers);
		//List<Product> listProducts = proservice.listAll();
	//	model.addAttribute("listProducts", listProducts);
		//model.addAllAttributes("user", user);
		return "redirect:/user_order_menu?added";
	}
	
	@RequestMapping("/delete_cart_item/{id}")
	public String deleteCartItem(@PathVariable(name="id") Long id, Model model) {
		User u = (User) model.getAttribute("user");
		uService.deleteCartItem(u, id);
		return "redirect:/user_view_cart?deleted";
	}
	
	/*@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("edit_product");
		Product product = proservice.get(id);
		mav.addObject("product", product);
		
		return mav;
	}*/


	
	

	
	/*

	
	
	@RequestMapping("/")
	public String viewHomePage(Model model) {
		List<Product> listProducts = pservice.listAll();
		model.addAttribute("listProducts", listProducts);
		
		return "show";
	}
	/*
	
	
	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") int id) {
		service.delete(id);
		return "redirect:/";		
	}
	*/
}
