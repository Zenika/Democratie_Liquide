package com.zenika.si.core.zenika.authentication.spring.social;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import com.zenika.si.core.zenika.authentication.AppConfig;

@Component
public final class SimpleSignInAdapter implements SignInAdapter {

	@Autowired
	private RememberMeServices rememberMeServices;
	@Autowired
	private SocialUserDetailsService socialUserDetailsService;

	@Autowired
	private AppConfig appConfig;

	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		SocialUserDetails socialUserDetails = socialUserDetailsService.loadUserByUserId(userId);

		if (socialUserDetails.isEnabled()) {
			SocialAuthenticationToken authentication = new SocialAuthenticationToken(connection, socialUserDetails,
					null, socialUserDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			rememberMeServices.loginSuccess(request.getNativeRequest(HttpServletRequest.class),
					request.getNativeResponse(HttpServletResponse.class), authentication);
			return appConfig.getRedirectUrl();
		} else {
			SecurityContextHolder.getContext().setAuthentication(null);
			rememberMeServices.loginFail(request.getNativeRequest(HttpServletRequest.class),
					request.getNativeResponse(HttpServletResponse.class));
			return appConfig.getRedirectUrlFailure();
		}

	}

}