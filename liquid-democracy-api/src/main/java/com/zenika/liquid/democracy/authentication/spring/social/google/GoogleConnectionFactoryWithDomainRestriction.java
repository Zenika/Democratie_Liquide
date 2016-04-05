package com.zenika.liquid.democracy.authentication.spring.social.google;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleAdapter;

public class GoogleConnectionFactoryWithDomainRestriction extends OAuth2ConnectionFactory<Google> {

	public GoogleConnectionFactoryWithDomainRestriction(String clientId, String clientSecret) {
		super("google", new GoogleServiceProviderWithDomainRestriction(clientId, clientSecret), new GoogleAdapter());
	}

}
