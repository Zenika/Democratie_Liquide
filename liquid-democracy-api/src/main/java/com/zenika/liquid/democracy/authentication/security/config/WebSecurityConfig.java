package com.zenika.liquid.democracy.authentication.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.social.security.SpringSocialConfigurer;

import com.zenika.liquid.democracy.authentication.AppConfig;
import com.zenika.liquid.democracy.authentication.spring.social.google.GoogleConfigurerAdapter;

@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Profile({ "prod", "dev" })
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	RememberMeServices rememberMeServices;
	@Autowired
	AppConfig appConfig;

	@Autowired
	GoogleConfigurerAdapter googleConfigurerAdapter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		SpringSocialConfigurer springSocialConfigurer = new SpringSocialConfigurer();
		http.exceptionHandling().and().authorizeRequests().antMatchers("/signin/google").permitAll()
				.antMatchers("/api/**").hasRole("ZENIKA").anyRequest().fullyAuthenticated().and().formLogin().disable()
				.anonymous().and().logout().logoutUrl("/signout/google").deleteCookies("JSESSIONID")
				.logoutSuccessUrl(appConfig.getRedirectUrlFailure()).and().rememberMe()
				.rememberMeServices(rememberMeServices).key(appConfig.getRememberMeKey()).and().csrf().disable()
				.apply(springSocialConfigurer);
	}
}