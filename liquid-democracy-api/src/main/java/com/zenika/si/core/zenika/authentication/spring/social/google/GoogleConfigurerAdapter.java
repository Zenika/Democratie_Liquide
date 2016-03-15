package com.zenika.si.core.zenika.authentication.spring.social.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;

import com.zenika.si.core.zenika.authentication.persistence.CollaboratorRepository;
import com.zenika.si.core.zenika.authentication.spring.social.AccountConnectionSignUp;

@Configuration
@EnableConfigurationProperties(GoogleProperties.class)
public class GoogleConfigurerAdapter extends SocialConfigurerAdapter {

	@Autowired
	private GoogleProperties properties;
	@Autowired
	private CollaboratorRepository userRepository;

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		InMemoryUsersConnectionRepository inMemoryUsersConnectionRepository = new InMemoryUsersConnectionRepository(
				connectionFactoryLocator);
		inMemoryUsersConnectionRepository.setConnectionSignUp(new AccountConnectionSignUp(userRepository));
		return inMemoryUsersConnectionRepository;
	}

	@Bean
	public UsersConnectionRepository usersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		return getUsersConnectionRepository(connectionFactoryLocator);
	}

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
		GoogleConnectionFactoryWithDomainRestriction factory = new GoogleConnectionFactoryWithDomainRestriction(
				this.properties.getAppId(), this.properties.getAppSecret());
		factory.setScope("email profile");
		configurer.addConnectionFactory(factory);
	}
}