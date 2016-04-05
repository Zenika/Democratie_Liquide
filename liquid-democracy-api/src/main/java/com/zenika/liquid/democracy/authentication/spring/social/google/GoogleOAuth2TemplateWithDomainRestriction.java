package com.zenika.si.core.zenika.authentication.spring.social.google;

import java.util.List;

import org.springframework.social.google.connect.GoogleOAuth2Template;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;

public class GoogleOAuth2TemplateWithDomainRestriction extends GoogleOAuth2Template {

	public GoogleOAuth2TemplateWithDomainRestriction(String clientId, String clientSecret) {
		super(clientId, clientSecret);
	}

	@Override
	public String buildAuthorizeUrl(OAuth2Parameters parameters) {
		addDomainToParameters(parameters);
		return super.buildAuthorizeUrl(parameters);
	}

	@Override
	public String buildAuthorizeUrl(GrantType grantType, OAuth2Parameters parameters) {
		addDomainToParameters(parameters);
		return super.buildAuthorizeUrl(grantType, parameters);
	}

	@Override
	public String buildAuthenticateUrl(OAuth2Parameters parameters) {
		addDomainToParameters(parameters);
		return super.buildAuthenticateUrl(parameters);
	}

	@Override
	public String buildAuthenticateUrl(GrantType grantType, OAuth2Parameters parameters) {
		addDomainToParameters(parameters);
		return super.buildAuthenticateUrl(grantType, parameters);
	}

	private void addDomainToParameters(OAuth2Parameters parameters) {
		if (parameters == null) {
			parameters = new OAuth2Parameters();
		}
		List<String> hd = parameters.get("hd");
		if (hd == null || hd.isEmpty())
			parameters.add("hd", "zenika.com");
	}

}
