package com.zenika.si.core.zenika.authentication.security.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;

import com.zenika.si.core.zenika.model.Collaborator;

public class SocialUserDetailsImpl implements SocialUserDetails, UserDetails {

	private Collaborator user;

	public SocialUserDetailsImpl(Collaborator user) {
		this.user = user;
	}

	@Override
	public String getUserId() {
		return user.getCollaboratorId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ZENIKA"));
		return grantedAuthorities;
	}

	public String getEmail() {
		return user.getEmail();
	}

	public String getDisplayName() {
		return user.getFirstName() + " " + user.getLastName();
	}

	@Override
	public String getPassword() {
		return user.getEmail();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return getEmail().endsWith("zenika.com");
	}

	public Collaborator getUser() {
		return user;
	}
}
