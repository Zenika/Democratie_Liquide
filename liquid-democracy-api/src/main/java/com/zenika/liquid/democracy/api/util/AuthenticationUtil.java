package com.zenika.liquid.democracy.api.util;

import java.util.Map;

import org.json.JSONArray;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class AuthenticationUtil {

	public static String getUserIdentifiant(OAuth2Authentication authentication) {
		Map<String, Object> details = (Map<String, Object>) authentication.getUserAuthentication().getDetails();

		JSONArray emailArray = new JSONArray(details.get("emails").toString());

		Map<String, Object> zenikaEmail = (Map<String, Object>) emailArray.get(0);

		return zenikaEmail.get("value").toString();
	}

}
