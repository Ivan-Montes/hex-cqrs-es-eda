package dev.ime.application.error;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.exception.BasicException;
import dev.ime.config.LoggerUtil;



@ControllerAdvice
public class ApplicationLevelExceptionHandler {

	private final LoggerUtil loggerUtil;	
	
	public ApplicationLevelExceptionHandler(LoggerUtil loggerUtil) {
		super();
		this.loggerUtil = loggerUtil;
	}

	@ExceptionHandler({
		dev.ime.application.exception.ResourceNotFoundException.class,
		dev.ime.application.exception.EntityAssociatedException.class,
		dev.ime.application.exception.EventUnexpectedException.class,
		dev.ime.application.exception.BasicException.class
		})
	public ResponseEntity<ExceptionResponse> basicException(BasicException ex){
	
		loggerUtil.logSevereAction(ex.getName() + " :=: Impl of Basic Exception Class");
		return new ResponseEntity<>( new ExceptionResponse( ex.getIdentifier(),ex.getName(),ex.getDescription(),ex.getErrors() ),
									HttpStatus.NOT_FOUND );
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionResponse> illegalArgumentException(IllegalArgumentException ex){
		
		loggerUtil.logSevereAction(ApplicationConstant.EX_ILLEGAL_ARGUMENT);
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				ApplicationConstant.EX_ILLEGAL_ARGUMENT,
				ApplicationConstant.EX_ILLEGAL_ARGUMENT_DESC,
				Map.of( ex.getLocalizedMessage(), ex.getMessage()) ),
				HttpStatus.BAD_REQUEST );
	}
	
}
