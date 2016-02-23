package com.zenika.liquid.democracy.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Malformed subject")
public class MalformedSubjectException extends Exception {

	private static final long serialVersionUID = 5208200175182322507L;

}
