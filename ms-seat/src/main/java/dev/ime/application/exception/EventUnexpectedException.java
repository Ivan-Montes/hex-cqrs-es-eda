package dev.ime.application.exception;

import java.util.Map;
import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;

public class EventUnexpectedException extends BasicException{

	private static final long serialVersionUID = -7876192023220281040L;

	public EventUnexpectedException(Map<String, String> errors) {
		super(
				UUID.randomUUID(),
				ApplicationConstant.EX_EVENT_UNEXPEC,
				ApplicationConstant.EX_EVENT_UNEXPEC_DESC,
				errors
				);
	}	

}
