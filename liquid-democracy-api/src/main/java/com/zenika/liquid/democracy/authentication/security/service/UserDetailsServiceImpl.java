package com.zenika.liquid.democracy.authentication.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private SocialUserDetailsService socialUserDetailsService;

	public void setSocialUserDetailsService(SocialUserDetailsService socialUserDetailsService) {
		this.socialUserDetailsService = socialUserDetailsService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		return socialUserDetailsService.loadUserByUserId(username);
	}
}
