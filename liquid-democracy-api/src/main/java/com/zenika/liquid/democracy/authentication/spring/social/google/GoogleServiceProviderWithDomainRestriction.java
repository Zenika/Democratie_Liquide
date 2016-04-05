package com.zenika.liquid.democracy.authentication.spring.social.google;

import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class GoogleServiceProviderWithDomainRestriction extends AbstractOAuth2ServiceProvider<Google> {

	public GoogleServiceProviderWithDomainRestriction(String clientId, String clientSecret) {
		super(new GoogleOAuth2TemplateWithDomainRestriction(clientId, clientSecret));
	}

	@Override
	public Google getApi(String accessToken) {
		return new GoogleTemplate(accessToken);
	}

}
