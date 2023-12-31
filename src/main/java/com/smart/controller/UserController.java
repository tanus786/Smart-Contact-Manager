package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

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
		// Get the user using userName(email)
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

	// Showing specific Contact Information
	@GetMapping("/contact/{cId}")
	public String showContactDetails(@PathVariable("cId") Integer cId, Model m, Principal principal) {
		System.out.println(cId);
		Optional<Contact> optional = this.contactRepository.findById(cId);
		Contact contact = optional.get();
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		if (user.getId() == contact.getUser().getId()) {
			m.addAttribute("contact", contact);
			m.addAttribute("title", contact.getName());
		}
		return "normal/contactDetails";
	}

	// Delete the Contact
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model m, Principal principal) {
		Optional<Contact> optional = this.contactRepository.findById(cid);
		Contact contact = optional.get();
		this.contactRepository.delete(contact);
		m.addAttribute("msg", new Message("Contact Deleted Successfully!!", "alert-success"));
		return "redirect:/user/viewContacts/0";
	}

	// Update Contact Handler
	@PostMapping("/update/{cid}")
	public String updateContact(@PathVariable("cid") Integer cid, Model m) {
		m.addAttribute("title", "Update Contact");
		Contact contact = this.contactRepository.findById(cid).get();
		m.addAttribute("contact", contact);
		return "normal/update";
	}

	// Profile Handler
	@GetMapping("/profile")
	public String profile(Model m) {
		m.addAttribute("title", "Profile");
		return "normal/profile";
	}

	// Update contact processing
//	@PostMapping("/processUpdate")
//	public String processUpdate(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
//			Model m, Principal principal) {
//
//		try {
//			if (!file.isEmpty()) {
//
//			}
//			User user = this.userRepository.getUserByUserName(principal.getName());
//			System.out.println(user);
//			contact.setUser(user);
//			Contact save = this.contactRepository.save(contact);
//			System.out.println(contact);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//
//		return "";
//	}
}
