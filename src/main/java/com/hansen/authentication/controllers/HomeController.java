package com.hansen.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hansen.authentication.models.User;
import com.hansen.authentication.services.UserService;
import com.hansen.authentication.validator.UserValidator;

@Controller
public class HomeController {
	
	private final UserService uService;
	private final UserValidator userValidator;	

	public HomeController(UserService uService, UserValidator userValidator) {
		super();
		this.uService = uService;
		this.userValidator = userValidator;
	}

	@GetMapping("/")
	public String index(@ModelAttribute("user") User user) {
		return "index.jsp";
	}
	
	@PostMapping("/registration")
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult result, HttpSession session) {
		userValidator.validate(user, result);
		if (result.hasErrors()) {
			return "index.jsp";
		} else {
			User u = this.uService.registerUser(user);
			session.setAttribute("userId", u.getId());
			return "redirect:/dashboard";
		}
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		Long id = (Long) session.getAttribute("userId");				
		model.addAttribute("user", this.uService.findUserById(id));
		return "dashboard.jsp";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("email") String email,
			@RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
		
		Boolean isLegit =  this.uService.authenticateUser(email, password);
		
		if (isLegit) {
			User user = this.uService.findByEmail(email);
			session.setAttribute("userId", user.getId());
			return "redirect:/dashboard";
		}
		redirectAttributes.addFlashAttribute("error", "Invalid Login Attempt.");
		return "redirect:/";
	}
	
	
	

}



















