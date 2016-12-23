package com.zenika.liquid.democracy.authentication.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.zenika.liquid.democracy.authentication.persistence.CollaboratorRepository;
import com.zenika.liquid.democracy.authentication.security.bean.SocialUserDetailsImpl;
import com.zenika.si.core.zenika.model.Collaborator;

@Service
@Profile({"!prod"})
public class DevSocialUserDetailsServiceImpl implements SocialUserDetailsService {

    @Autowired
    private CollaboratorRepository userRepository;

    public void setUserRepository(CollaboratorRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) {

        Collaborator user = new Collaborator();

        Optional<Collaborator> userTmp = userRepository.findCollaboratorByEmail(userId);

        if (userTmp.isPresent()) {
            user = userTmp.get();
        } else if (userId.endsWith("@zenika.com")) {
            user.setEmail(userId);
            user.setFirstName("Dave");
            user.setLastName("Lauper");

            userRepository.save(user);
        }

        return new SocialUserDetailsImpl(user);
    }
}
