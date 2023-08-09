package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
public class HomeController {
	@RequestMapping("/home")
	public String home(Model m) {
		m.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	
	
	
	
	
	
//	@Autowired
//	private UserRepository userRepository;
//	
//	@GetMapping("/test")
//	@ResponseBody
//	public String Test() {
//		User user = new User();
//		user.setName("Tanu");
//		user.setEmail("abc@gmail.com");
//		Contact contact = new Contact();
//		user.getContacts().add(contact);
//		userRepository.save(user);
//		return "Working";
//	}
}
