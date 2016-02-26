package com.zenika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableOAuth2Sso
public class Application extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// @Override
	// public void addViewControllers(ViewControllerRegistry registry) {
	// registry.addViewController("/home").setViewName("index");
	// //
	// registry.addViewController("/login_success").setViewName("login_success");
	// }

	// @Bean
	// public InternalResourceViewResolver viewResolver() {
	// InternalResourceViewResolver viewResolver = new
	// InternalResourceViewResolver();
	// viewResolver.setViewClass(JstlView.class);
	// viewResolver.setSuffix(".jsp");
	// return viewResolver;
	// }

}
