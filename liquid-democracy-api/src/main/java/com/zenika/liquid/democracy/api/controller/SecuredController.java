package com.zenika.liquid.democracy.api.controller;

import org.springframework.security.core.context.SecurityContextHolder;

import com.zenika.si.core.zenika.authentication.security.bean.SocialUserDetailsImpl;
import com.zenika.si.core.zenika.model.Collaborator;

public abstract class SecuredController {

	protected SocialUserDetailsImpl getAuthenticatedUser() {
		return (SocialUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	protected Collaborator currentUser() {
		return getAuthenticatedUser().getUser();
	}
}
