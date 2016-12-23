package com.zenika.liquid.democracy.authentication.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;

import com.zenika.liquid.democracy.authentication.AppConfig;

@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    RememberMeServices rememberMeServices;

    @Autowired
    AppConfig appConfig;

    @Autowired
    private LiquidSecurityConfigurer liquidSecurityConfigurer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        LOG.warn("configure and security configurer is {}", liquidSecurityConfigurer.getClass());

        liquidSecurityConfigurer.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        liquidSecurityConfigurer.configure(auth);
    }
}