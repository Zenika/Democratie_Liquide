package com.zenika.liquid.democracy.authentication;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {

	@NotNull
	private String rememberMeKey;

	@NotNull
	private String redirectUrl;

	@NotNull
	private String redirectUrlFailure;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getRememberMeKey() {
		return rememberMeKey;
	}

	public void setRememberMeKey(String rememberMeKey) {
		this.rememberMeKey = rememberMeKey;
	}

	public String getRedirectUrlFailure() {
		return redirectUrlFailure;
	}

	public void setRedirectUrlFailure(String redirectUrlFailure) {
		this.redirectUrlFailure = redirectUrlFailure;
	}
}
