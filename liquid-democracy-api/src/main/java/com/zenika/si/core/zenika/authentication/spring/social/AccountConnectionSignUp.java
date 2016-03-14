package com.zenika.si.core.zenika.authentication.spring.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import com.zenika.si.core.zenika.authentication.persistence.CollaboratorRepository;
import com.zenika.si.core.zenika.model.Collaborator;

public class AccountConnectionSignUp implements ConnectionSignUp {

	@Autowired
	private final CollaboratorRepository userRepository;

	public AccountConnectionSignUp(CollaboratorRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public String execute(Connection<?> connection) {
		UserProfile profile = connection.fetchUserProfile();
		Collaborator user = userRepository.findCollaboratorByEmail(profile.getEmail());
		if (user == null) {
			user = new Collaborator();
			user.setEmail(profile.getEmail());
			user.setUsername(profile.getFirstName());
			userRepository.insert(user);
		}
		return user.getEmail();
	}

}