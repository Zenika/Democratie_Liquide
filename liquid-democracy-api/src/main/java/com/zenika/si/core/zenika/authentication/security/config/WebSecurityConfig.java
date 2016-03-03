package com.zenika.si.core.zenika.authentication.security.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile({ "test-prod" })
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/*").hasAuthority("ROLE_ZENIKA").and().logout().permitAll().and();
		http.httpBasic();
		http.csrf().disable();
	}

	@Bean
	public AuthoritiesExtractor authoritiesExtractor(OAuth2RestOperations template) {
		return map -> {
			String domain = (String) map.get("domain");
			if (!StringUtils.isEmpty(domain) && domain.equals("zenika.com")) {
				return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ZENIKA");
			} else {
				return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_EXTERN");
			}
		};
	}

}
