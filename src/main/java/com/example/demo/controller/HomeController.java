package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.KitchenDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Kitchen;
import com.example.demo.entity.User;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.KitchenServiceImpl;
import com.example.demo.service.UserService;

@Controller
public class HomeController {

	@Autowired
	AuthenticationService aService;
	
	@Autowired
	KitchenServiceImpl kService;
	
	@Autowired
	UserService uService;
	
    @GetMapping("/")
    public String root() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
    	Object o = new Object();
    	model.addAttribute("object", o);
    	//System.out.println("email: " + k.getEmail());
    	//System.out.println("pass: " + k.getPassword());
        return "login";
    }
    
    @GetMapping("/kitchen_login")
    public String kitchenLogin(Model model) {
    	Kitchen k = new Kitchen();
    	model.addAttribute("kitchen", k);
    	//System.out.println("email: " + k.getEmail());
    	//System.out.println("pass: " + k.getPassword());
        return "kitchen_login";
    }
    
    // Don't think these do anything
    @PostMapping("/kitchen_login")
    public String loginSubmit(@ModelAttribute("kitchenlogin") Kitchen k, Model model,
    		RedirectAttributes redirectAttributes) {
    	List<Kitchen> list = kService.findKitchenList();
    	for (int i = 0; i < list.size(); i++) {
    		if (list.get(i).equals(k)) {
    			redirectAttributes.addFlashAttribute("kitchen", list.get(i));
    			return "redirect:/edit_menu";
    		}
    	}	
    	return "redirect:/kitchen_login?error";
    }
    
    @GetMapping("/kitchen_registration")
	public String showForm(Model model) {
		KitchenDto dto = new KitchenDto();
		model.addAttribute("kitchen_dto", dto);
		/*
		 * Kitchen k = new Kitchen(); List<Kitchen> list = kService.getKitchenList();
		 * for (int i = 0; i < list.size(); i++) { if (list.get(i).equals(kitchen)) { k
		 * = list.get(i); break; } }
		 */
		// Kitchen kitchen = new Kitchen();
		// model.addAttribute("kitchen", kitchen);
		// Kitchen k = (Kitchen) model.getAttribute("kitchen");
		// kitchen.setId(kService.getKitchenId(kitchen));
		// System.out.println("id in page 1: " + k.getId());
		// System.out.println("id in page 1: " + kitchen.getId());
		return "kitchen_registration_form";
	}

	@PostMapping("/kitchen_registration")
	public String submitForm(@Valid @ModelAttribute("kitchen_dto") KitchenDto dto,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		Kitchen existing = kService.findByEmail(dto.getEmail());
        if (existing != null){
        	bindingResult.rejectValue("email", null, "There is already an account registered with that email");
        }
        
		if (bindingResult.hasErrors())
			return "kitchen_registration_form";
		else {
			aService.saveKitchen(dto);
			/*Kitchen k = new Kitchen();
			List<Kitchen> list = kService.getKitchenList();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getEmail().equals(kitchenDto.getEmail())) {
					k = list.get(i);
					// System.out.println("Id:" + k.getId());
					// System.out.println("Name: " + k.getName());
					// System.out.println("Email: " + k.getEmail());
					// System.out.println("Password: " + k.getPassword());
					break;
				}
				// model.addAttribute("kitchen", k);
				redirectAttributes.addFlashAttribute("kitchen", k);
			}*/
			return "redirect:/?success";
		}
	}
    
    @GetMapping("/user_login")
    public String userLogin(Model model) {
		User u = new User();
		model.addAttribute("user", u);
		return "user_login";
	}
    
    // Don't think these do anything
    @PostMapping("/user_login")
    public String loginSubmit(@ModelAttribute("user") User u, Model model,
    		RedirectAttributes redirectAttributes) {
    	List<User> list = uService.findUserList();
    	for (int i = 0; i < list.size(); i++) {
    		if (list.get(i).equals(u)) {
    			redirectAttributes.addFlashAttribute("user", list.get(i));
    			return "redirect:/user_homepage";
    		}
    	}	
    	return "redirect:/user_login?error";
    }
	
	@GetMapping("/user_registration")
	public String registration(Model model) {
		//User user = new User();
		//model.addAttribute("user", user);	
		UserDto dto = new UserDto();
		model.addAttribute("user_dto", dto);
		return "user_registration_form";
	}
	
	@PostMapping("/user_registration")
	public String registrationSubmit(@Valid @ModelAttribute("user_dto") UserDto dto,
			BindingResult bindingResult) {	
		User existing = uService.findByEmail(dto.getEmail());
        if (existing != null){
        	bindingResult.rejectValue("email", null, "There is already an account registered with that email");
        }
        
		if (bindingResult.hasErrors())
			return "user_registration_form";
		else {
			aService.saveUser(dto);
			return "redirect:/?success";
		}
	}
}
