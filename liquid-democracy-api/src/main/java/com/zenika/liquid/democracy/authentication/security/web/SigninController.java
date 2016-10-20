package com.zenika.liquid.democracy.authentication.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import com.zenika.liquid.democracy.authentication.security.config.FormSecurityConfigurer;
import com.zenika.liquid.democracy.authentication.security.config.GoogleFormSecurityConfigurer;
import com.zenika.liquid.democracy.authentication.security.config.GoogleSecurityConfigurer;
import com.zenika.liquid.democracy.authentication.security.config.LiquidSecurityConfigurer;

/**
 * @author Guillaume Gerbaud
 */
@RestController
@RequestMapping("/signin")
public class SigninController {

    private static final String FORM_AUTH = "FORM_AUTH";
    private static final String GOOGLE_AUTH = "GOOGLE_AUTH";

    private ApplicationContext context;

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/methods")
    public Map<String, Boolean> getAvailableAuthMethods() {

        Map<String, Boolean> methods = new HashMap<>();

        LiquidSecurityConfigurer bean = context.getBean(LiquidSecurityConfigurer.class);
        if (bean != null) {
            methods.put(GOOGLE_AUTH, bean instanceof GoogleFormSecurityConfigurer || bean instanceof GoogleSecurityConfigurer);
            methods.put(FORM_AUTH, bean instanceof GoogleFormSecurityConfigurer || bean instanceof FormSecurityConfigurer);
        }

        return methods;
    }
}
