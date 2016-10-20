package com.zenika.liquid.democracy.authentication.security.config;

import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * @author Guillaume Gerbaud
 */
public interface LiquidSecurityConfigurer extends SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {

    default void configure(AuthenticationManagerBuilder auth) throws Exception {
    }
}
