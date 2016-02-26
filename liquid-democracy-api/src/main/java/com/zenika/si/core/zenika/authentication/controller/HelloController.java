package com.zenika.si.core.zenika.authentication.controller;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/login_success")
	public String hello(OAuth2Authentication info) {
		// TODO : connexion bdd
		return "";
	}

	@RequestMapping("/")
	public String helloRacine(OAuth2Authentication info, Model model) {
		model.addAttribute("user", info.getName());
		return info.getUserAuthentication().toString();
	}

}
