package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Signup - Smart Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}

	// Handler for Registration of user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agrement", defaultValue = "false") boolean agrement, Model m) {
		try {

			if (result.hasErrors()) {
				m.addAttribute("user", user);
				return "signup";
			}
			if (!agrement) {
//				throw new Exception("Please accept terms and conditions!!");
				m.addAttribute("message", new Message("Please accept terms and conditions!!", "alert-danger"));
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User save = this.userRepository.save(user);
			m.addAttribute("user", save);
			m.addAttribute("message", new Message("Successfully Registered!!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			m.addAttribute("message", new Message("Something went wrong!!" + e.getMessage(), "alert-danger"));
			return "signup";
		}
		return "signup";
	}
/*
	// Handler for custom Login
	@GetMapping("/signin")
	public String customLogin(Model m) {
		m.addAttribute("title", "Login Page");
		return "login";
	}
	*/
}
