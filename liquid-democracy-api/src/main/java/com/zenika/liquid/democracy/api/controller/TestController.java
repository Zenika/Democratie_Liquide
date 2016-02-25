package com.zenika.liquid.democracy.api.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

	@RequestMapping(value = "/userHome", method = RequestMethod.GET)
	public String getUserProfile(Model model) {
		model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		model.addAttribute("password", "sparlant");
		return "entra.html";
	}

	@RequestMapping(value = "/return?code={codeParam}", method = RequestMethod.GET)
	public String getUserProfile(@PathVariable String codeParam) {
		return "entra.html";
	}

	@RequestMapping(value = "/login")
	public String getIndex() {
		return "index.html";
	}

}
