package com.zenika;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zenika.si.core.zenika.authentication.AppConfig;
import com.zenika.si.core.zenika.authentication.service.CollaboratorService;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

	@Autowired
	private AppConfig appConfig;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
		// objectMapper.setDateFormat(new
		// SimpleDateFormat(ConstantKt.getMAIN_DATE_FORMAT()));
		return objectMapper;
	}

	@Bean
	public RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
		String key = appConfig.getRememberMeKey();
		TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices(key,
				userDetailsService);
		tokenBasedRememberMeServices.setAlwaysRemember(true);
		return tokenBasedRememberMeServices;
	}

	@Bean
	public FilterRegistrationBean authorizationFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setOrder(-100000);
		filterRegBean.setFilter(new CorsFilter());
		filterRegBean.addInitParameter("cors.allowed.headers",
				"Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
		filterRegBean.addInitParameter("cors.allowed.methods", "GET,POST,HEAD,OPTIONS,DELETE,PUT");
		List<String> urlPatterns = new ArrayList<>();
		urlPatterns.add("/*");
		filterRegBean.setUrlPatterns(urlPatterns);
		return filterRegBean;
	}

	@Bean
	public CollaboratorService collaboratorService() {
		return new CollaboratorService();
	}

}
