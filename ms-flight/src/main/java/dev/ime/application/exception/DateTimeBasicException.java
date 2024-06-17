package dev.ime.application.exception;

import java.util.Map;
import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;

public class DateTimeBasicException  extends BasicException{

	private static final long serialVersionUID = 8726077077252036507L;

	public DateTimeBasicException( Map<String, String> errors ) {
		super(
				UUID.randomUUID(), 
				ApplicationConstant.EX_DATETIME, 
				ApplicationConstant.EX_DATETIME_DESC,
				errors
				);
	}

}
