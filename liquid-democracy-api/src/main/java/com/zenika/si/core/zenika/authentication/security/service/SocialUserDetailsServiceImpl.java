package com.zenika.si.core.zenika.authentication.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import com.zenika.si.core.zenika.authentication.persistence.CollaboratorRepository;
import com.zenika.si.core.zenika.authentication.security.bean.SocialUserDetailsImpl;
import com.zenika.si.core.zenika.model.Collaborator;

@Service
public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

	@Autowired
	private CollaboratorRepository userRepository;

	public void setUserRepository(CollaboratorRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) {
		Collaborator user = userRepository.findCollaboratorByEmail(userId);

		if (user == null) {
			user = new Collaborator();
		}

		return new SocialUserDetailsImpl(user);
	}
}