package com.zenika.si.core.zenika.authentication.spring.social;

import java.util.Optional;

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
		Collaborator user = new Collaborator();

		UserProfile profile = connection.fetchUserProfile();

		Optional<Collaborator> userTmp = userRepository.findCollaboratorByEmail(profile.getEmail());

		if (userTmp.isPresent()) {
			user = userTmp.get();
		} else {
			user.setEmail(profile.getEmail());
			user.setFirstName(profile.getFirstName());
			user.setLastName(profile.getLastName());
			userRepository.insert(user);
		}

		return user.getEmail();
	}

}