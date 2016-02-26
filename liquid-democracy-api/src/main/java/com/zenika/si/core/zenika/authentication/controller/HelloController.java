package com.zenika.si.core.zenika.authentication.controller;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/login_success")
	public String hello(OAuth2Authentication authentication) {
		return authentication.getName();
	}

	@RequestMapping("/")
	public String helloRacine(OAuth2Authentication authentication, Model model) {
		model.addAttribute("user", authentication.getName());
		model.addAttribute("authentication", authentication.getUserAuthentication());
		return authentication.getUserAuthentication().toString();
	}

}
