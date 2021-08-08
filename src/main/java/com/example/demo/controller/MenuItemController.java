package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Kitchen;
import com.example.demo.entity.MenuItem;
import com.example.demo.service.KitchenServiceImpl;
import com.example.demo.service.MenuItemServiceImpl;

@Controller
//@Transactional
@SessionAttributes("kitchen")
public class MenuItemController {
	
	@Autowired
	MenuItemServiceImpl mService;
	
	@Autowired
	KitchenServiceImpl kService;
	
	//Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C:\\Users\\katri\\OneDrive\\Pictures\\project-imgs";
    
	/*@ModelAttribute("kitchen")
	public Kitchen kitchen(RedirectAttributes redirectAttributes, Model model) {
		System.out.println("made it in here");
		Kitchen k = (Kitchen) redirectAttributes.getAttribute("login");
		System.out.println("retrieved redirect");
		//model.addAttribute("kitchen", k);
		//System.out.println("added model");
		return k;
	}*/
	
	@GetMapping("/edit_menu")
	public String editMenu(Model model) {
		//MenuItem menu = new MenuItem();
		//model.addAttribute("menu", menu);
		Kitchen k = (Kitchen) model.getAttribute("kitchen");
		System.out.println("k_id in /edit_menu: " + k.getId());
		//System.out.println("k_id in edit_menu: " + kitchen.getId());
		//System.out.println("Name: " + kitchen.getName());
		//System.out.println("Email: " + kitchen.getEmail());
		//System.out.println("Password: " + kitchen.getPassword());
		List<MenuItem> list = mService.findMenu(k);
		k.setMenu(list);
		model.addAttribute("items", list);
		//model.addAttribute("kitchen", kitchen);
		//System.out.println("k_id in edit_menu: " + kitchen.getId());
		//System.out.println("Name: " + kitchen.getName());
		//System.out.println("Email: " + kitchen.getEmail());
		//System.out.println("Password: " + kitchen.getPassword());
		return "kitchen_settings_menu_edit";
	}
	
	@GetMapping("/add_menu_item")
	public String addMenuItem(Model model) {
		Kitchen k = (Kitchen) model.getAttribute("kitchen");
		MenuItem menu = new MenuItem();
		menu.setKitchen(k);
		//System.out.println("before model ID: " + menu.getId());
		model.addAttribute("menu", menu);
		//System.out.println("after model ID: " + menu.getId());
		//System.out.println("Name: " + kitchen.getName());
		//System.out.println("Email: " + kitchen.getEmail());
		//System.out.println("Password: " + kitchen.getPassword());
		return "kitchen_settings_menu_add_item";
	}
	
	@PostMapping("/add_menu_item")
	public String saveMenuItem(@Valid @ModelAttribute("menu") MenuItem menu, Model model,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		Kitchen k = (Kitchen) model.getAttribute("kitchen");
		//System.out.println("ID: " + kitchen.getId());
		//System.out.println("Name: " + kitchen.getName());
		//System.out.println("Email: " + kitchen.getEmail());
		//System.out.println("Password: " + kitchen.getPassword());
		//System.out.println("error in savemenu");
		//String test = StringUtils.cleanPath(file.getOriginalFilename());
		//System.out.println(test);
		if (bindingResult.hasErrors())
			return "kitchen_settings_menu_add_item";
		else {
			//System.out.println("error in savemenu save");
			//mService.saveMenuItem(kitchen.getId(), menu);
			//List<MenuItem> temp = new ArrayList<MenuItem>();
			//temp = kitchen.getMenu();
			//temp.add(menu);
			//kitchen.setMenu(temp);
			//System.out.println("k_id before all: " + kitchen.getId());
			//if (kitchen.getMenu() != null)
				//kitchen.getMenu().clear();
			//System.out.println("k_id after clear: " + kitchen.getId());
			k.addMenuItem(menu);
			//System.out.println("k_id after object method: " + kitchen.getId());
			mService.saveMenuItem(k);
			redirectAttributes.addFlashAttribute("kitchen", k);
			System.out.println("k_id after service: " + k.getId());
			//System.out.println("ID: " + menu.getId());
			//System.out.println("Item Name: " + menu.getItemName());
			//System.out.println("Veg: " + menu.isVeg());
			//System.out.println("Price: " + menu.getPrice());
			//kitchen.getMenu().add(menu);
			//System.out.println("error in savemenu redirect");
			return "redirect:/edit_menu";
		}
		//model.addAttribute("menu", menu);
		//return "kitchen_register_form_menu_add_item";
	}
	/*
	@PostMapping("/add_menu_item")
	public String saveItem(@ModelAttribute("kitchen") Kitchen kitchen, @ModelAttribute("menu") MenuItem menu,
			@RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
		System.out.println("error before fileName");
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println("filename conversion success");
		menu.setImageURL(fileName);
		kitchen.addMenuItem(menu);
		mService.saveMenuItem(kitchen);
		redirectAttributes.addFlashAttribute("kitchen", kitchen);
		FileUploadUtil.saveFile(UPLOADED_FOLDER, fileName, file);
		return "redirect:/edit_menu";
		//model.addAttribute("menu", menu);
		//return "kitchen_register_form_menu_add_item";
	}*/
	
	@RequestMapping("/list_items")
	public String listMenuItems(@ModelAttribute("kitchen") Kitchen k, Model model) {
		List<MenuItem> list = mService.findMenu(k);
		model.addAttribute("items", list);
		System.out.println("list items is used?");
		return "kitchen_registration_form_2";
	}
	
	@GetMapping("/view_menu")
	public String viewMenu(@ModelAttribute("kitchen") Kitchen kitchen, Model model) {
		//Kitchen k = (Kitchen) model.getAttribute("kitchen");
		//MenuItem menu = new MenuItem();
		//model.addAttribute("menu", menu);
		//Kitchen k = (Kitchen) model.getAttribute("kitchen");
		//System.out.println("k_id using model: " + k.getId());
		//System.out.println("k_id in edit_menu: " + kitchen.getId());
		//System.out.println("Name: " + kitchen.getName());
		//System.out.println("Email: " + kitchen.getEmail());
		//System.out.println("Password: " + kitchen.getPassword());
		List<MenuItem> list = mService.findMenu(kitchen);
		kitchen.setMenu(list);
		model.addAttribute("items", list);
		//System.out.println("k_id in edit_menu: " + kitchen.getId());
		//System.out.println("Name: " + kitchen.getName());
		//System.out.println("Email: " + kitchen.getEmail());
		//System.out.println("Password: " + kitchen.getPassword());
		return "kitchen_settings_menu_view";
	}
	
	@RequestMapping("/edit_item/{id}")
	public ModelAndView editMenuItem(@PathVariable(name="id") Long id, Model model) {
		Kitchen k = (Kitchen) model.getAttribute("kitchen");
		ModelAndView mav = new ModelAndView("kitchen_settings_menu_edit_item");
		System.out.println("crashed before service");
		System.out.println("k_id: " + k.getId());
		System.out.println("m_id: " + id);
		MenuItem m = mService.findMenuItem(k, id);
		System.out.println("item being edit: " + m.toString());
		mav.addObject("menu", m);
		return mav;
	}
	
	@RequestMapping(value="/menu_save", method = RequestMethod.POST)
	public String saveEdit(@Valid @ModelAttribute("menu") MenuItem m, Model model,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		Kitchen k = (Kitchen) model.getAttribute("kitchen");
		if (bindingResult.hasErrors())
			return "kitchen_settings_menu_edit_item";
		else {
			mService.editMenuItem(k, m);
			redirectAttributes.addFlashAttribute("kitchen", k);
			return "redirect:/edit_menu";
		}
	}
	
	@RequestMapping("/delete_item/{id}")
	public String deleteMenuItem(@PathVariable(name="id") Long id, Model model,
			RedirectAttributes redirectAttributes) {
		Kitchen k = (Kitchen) model.getAttribute("kitchen");
		//System.out.println("k_id is: " + k.getId());
		//System.out.println("m_id is: " + id);
		//System.out.println("menu items: " + k.getMenu().toString());
		//MenuItem m = mService.findMenuItem(kitchen, id);
		//kitchen.deleteMenuItem(m);
		//kService.saveKitchen(kitchen);
		//mService.saveMenuItem(k);
		mService.deleteMenuItem(k, id);
		redirectAttributes.addFlashAttribute("kitchen", k);
		//mService.deleteMenuItem(k.getId(), id);
		return "redirect:/edit_menu";
	}
}
