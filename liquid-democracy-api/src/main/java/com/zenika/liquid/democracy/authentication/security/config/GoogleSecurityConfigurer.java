package com.zenika.liquid.democracy.authentication.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.stereotype.Component;

import com.zenika.liquid.democracy.authentication.AppConfig;

/**
 * @author Guillaume Gerbaud
 */
@Component
@Profile({"prod"})
public class GoogleSecurityConfigurer implements LiquidSecurityConfigurer {

    @Autowired
    RememberMeServices rememberMeServices;

    @Autowired
    AppConfig appConfig;

    @Override
    public void init(HttpSecurity builder) throws Exception {
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        SpringSocialConfigurer springSocialConfigurer = new SpringSocialConfigurer();

        http.exceptionHandling()
                .and().authorizeRequests()
                .antMatchers("/signin/methods").permitAll()
                .antMatchers("/signin/google").permitAll()
                .antMatchers("/api/**").hasRole("ZENIKA").anyRequest().fullyAuthenticated()
                .and().formLogin().disable()
                .anonymous()
                .and().logout().logoutUrl("/signout").deleteCookies("JSESSIONID").logoutSuccessUrl(appConfig.getRedirectUrlFailure())
                .and().rememberMe().rememberMeServices(rememberMeServices).key(appConfig.getRememberMeKey())
                .and().csrf().disable()
                .apply(springSocialConfigurer);
    }
}
