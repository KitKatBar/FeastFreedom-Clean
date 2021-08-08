package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Kitchen;
import com.example.demo.entity.Mail;
import com.example.demo.service.EmailSenderService;
import com.example.demo.service.KitchenServiceImpl;


@Controller
//@Transactional
@SessionAttributes("kitchen")
public class KitchenController {

	@Autowired
	KitchenServiceImpl kService;
	
	@Autowired
	EmailSenderService emailService;

	//@ModelAttribute("kitchen")
	//public Kitchen kitchen() {
		//return new Kitchen();
	//}

	/*
	 * @ModelAttribute("kitchen_dto") public KitchenRegistrationDto kitchenDto() {
	 * return new KitchenRegistrationDto(); }
	 */

	@GetMapping("/kitchen_settings")
	public String showForm2(Principal principal, Model model) {
		String email = principal.getName();
		// System.out.println("email is: " + email);
		Kitchen k = new Kitchen();
		List<Kitchen> list = kService.findKitchenList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getEmail().equals(email)) {
				k = list.get(i);
				model.addAttribute("kitchen", k);
				break;
			}
		}
		
		List<Integer> listStartTime = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24);
        model.addAttribute("listStartTime", listStartTime);
        List<Integer> listEndTime = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24);
        model.addAttribute("listEndTime", listEndTime);
        
		// Kitchen k = (Kitchen) model.getAttribute("kitchen");
		// Kitchen k = (Kitchen) redirectAttributes.getAttribute("kitchen");
		// Kitchen k = new Kitchen();
		// k = kService.findKitchen(kitchen.getId());
		// model.addAttribute("kitchen", kitchen);
		// redirectAttributes.addFlashAttribute("kitchen", kitchen);
		// System.out.println("Id:" + kitchen.getId());
		// System.out.println("Name: " + kitchen.getName());
		// System.out.println("Email: " + kitchen.getEmail());
		// System.out.println("Password: " + kitchen.getPassword());
		// System.out.println("Name: " + k.getName());
		// System.out.println("Email: " + k.getEmail());
		// System.out.println("Password: " + k.getPassword());
		return "kitchen_settings_hours";
	}

	@PostMapping("/kitchen_settings")
	public String submitForm2(@ModelAttribute("kitchen") Kitchen kitchen, Model model, SessionStatus status,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		// Kitchen kitchen = (Kitchen) model.getAttribute("kitchen");
		// redirectAttributes.addFlashAttribute("kitchen", kitchen);
		if (bindingResult.hasErrors())
			return "kitchen_settings_hours";
		else {
			model.addAttribute("daysOpen", kitchen);
			model.addAttribute("starttime", kitchen);
			model.addAttribute("endtime", kitchen);
			kService.saveKitchen(kitchen);
			Mail mail = new Mail();
	        mail.setFrom("nicholasted200@gmail.com");//replace with your desired email
	        mail.setMailTo(kService.getKitchenEmail(kitchen));//replace with your desired email
	        //mail.setMailTo("nicholasdefrance@hotmail.com");//replace with your desired email
	        mail.setSubject("Email with Spring boot and thymeleaf template!");
	        Map<String, Object> model2 = new HashMap<String, Object>();
	        model2.put("name", kService.getKitchenName(kitchen));
	        model2.put("location", "United States");
	        model2.put("sign", "Java Developer");
	        mail.setProps(model2);
	        try {
				emailService.sendEmail(mail);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// redirectAttributes.addFlashAttribute("kitchen", kitchen);
			// System.out.println("id before pass: " + kitchen.getId());
			// System.out.println("Name: " + kitchen.getName());
			// System.out.println("Email: " + kitchen.getEmail());
			// System.out.println("Password: " + kitchen.getPassword());
			// request.getSession().setAttribute("kitchen", null);
			// request.removeAttribute("kitchen", WebRequest.SCOPE_SESSION);
			// status.setComplete();
			return "redirect:/kitchen_settings?saved";
		}
	}
	
	@GetMapping("/list_kitchens")
	public String getKitchenList(Model model) {
		List<Kitchen> list = kService.findKitchenList();
		model.addAttribute("kitchen", list);
		return "allindex"; // here its a view name,
	}
	
	@ModelAttribute("daysOpenList")
	   public List<String> getDaysOpenList() {
	      List<String> daysOpenList = new ArrayList<String>();
	      daysOpenList.add("Monday");
	      daysOpenList.add("Tuesday");
	      daysOpenList.add("Wednesday");
	      daysOpenList.add("Thursday");
	      daysOpenList.add("Friday");
	      daysOpenList.add("Saturday");
	      daysOpenList.add("Sunday");
	      return daysOpenList;
	}
}
