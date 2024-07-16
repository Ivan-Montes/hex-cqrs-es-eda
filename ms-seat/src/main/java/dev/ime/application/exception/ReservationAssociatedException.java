package dev.ime.application.exception;

import java.util.Map;
import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;

public class ReservationAssociatedException extends BasicException{

	private static final long serialVersionUID = -8650490709616728609L;

	public ReservationAssociatedException(Map<String, String> errors) {
		super(
				UUID.randomUUID(), 
				ApplicationConstant.EX_RESERVATION_ASSOCIATED, 
				ApplicationConstant.EX_RESERVATION_ASSOCIATED_DESC,
				errors
				);
	}

}
