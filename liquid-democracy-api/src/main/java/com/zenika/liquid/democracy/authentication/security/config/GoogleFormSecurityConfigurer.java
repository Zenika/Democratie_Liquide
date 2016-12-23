package com.zenika.liquid.democracy.authentication.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.stereotype.Component;

import com.zenika.liquid.democracy.authentication.AppConfig;
import com.zenika.liquid.democracy.authentication.security.config.cond.ConditionnalOnGoogleKey;

/**
 * @author Guillaume Gerbaud
 */
@Component
@Profile({"!prod"})
@ConditionnalOnGoogleKey
public class GoogleFormSecurityConfigurer implements LiquidSecurityConfigurer {

    @Autowired
    RememberMeServices rememberMeServices;

    @Autowired
    AppConfig appConfig;

    @Autowired
    private ApiAuthEntryPoint apiAuthEntryPoint;

    @Autowired
    private MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void init(HttpSecurity builder) throws Exception {
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        SpringSocialConfigurer springSocialConfigurer = new SpringSocialConfigurer();

        http.exceptionHandling().authenticationEntryPoint(apiAuthEntryPoint)
                .and().authorizeRequests()
                .antMatchers("/signin/methods").permitAll()
                .antMatchers("/signin/google").permitAll()
                .antMatchers("/api/**").hasRole("ZENIKA").anyRequest().fullyAuthenticated()
                .and().formLogin()
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .successHandler(mySimpleUrlAuthenticationSuccessHandler)
                .usernameParameter("email")
                .loginProcessingUrl("/signin/form")
                .permitAll()
                .and().anonymous()
                .and().logout().logoutUrl("/signout").deleteCookies("JSESSIONID").logoutSuccessUrl(appConfig.getRedirectUrlFailure())
                .and().rememberMe().rememberMeServices(rememberMeServices).key(appConfig.getRememberMeKey())
                .and().csrf().disable()
                .apply(springSocialConfigurer);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
