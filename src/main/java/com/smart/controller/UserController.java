package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

//Method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
//		System.out.println(userName);
		// Get the use using userName(email)
		User user = userRepository.getUserByUserName(userName);
//		System.err.println(user);
		model.addAttribute("user", user);
	}

	// Home dashBoard
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "Home || Dashboard");
		return "normal/user_dashboard";
	}

	// Add contact Controller
	@GetMapping("/addContact")
	public String addContact(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		return "normal/addContact";
	}

	// Add Contact Handler
	@PostMapping("/process-contact")
	public String addContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, Model model) {
		try {
			System.out.println(contact);
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			// Processing Image
			if (file.isEmpty()) {
				// Show your msg
				contact.setImage("default.png");
				System.out.println("Image not uploaded");
			} else {
				// upload the file in the folder and add name in the contact
				contact.setImage(file.getOriginalFilename());
				File savefile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image uploaded");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("Added");
			model.addAttribute("msg", new Message("Contact Added Successfully!!", "alert-success"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("msg", new Message("Something Went Wrong!!", "alert-danger"));
		}
		return "normal/addContact";
	}

	// Show Contact Handler
	// Per page = 5
	// Current page = 0[page]
	@GetMapping("/viewContacts/{page}")
	public String viewContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "All Contacts");

		// Sending Contact list from database to viewContacts page
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		PageRequest pageable = PageRequest.of(page, 5);
		Page<Contact> allContacts = this.contactRepository.findContactByUser(user.getId(), pageable);
		m.addAttribute("contacts", allContacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", allContacts.getTotalPages());

//		String userName = principal.getName();
//		User user = this.userRepository.getUserByUserName(userName);
//		List<Contact> contacts = user.getContacts();
		return "normal/viewContacts";
	}

}
