package dev.ime.infrastructure.error;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.error.ExceptionResponse;
import dev.ime.config.LoggerUtil;

@ControllerAdvice
public class InfrastructureLevelExceptionHandler {

	private final LoggerUtil loggerUtil;	
	
	public InfrastructureLevelExceptionHandler(LoggerUtil loggerUtil) {
		super();
		this.loggerUtil = loggerUtil;
	}

	@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex){

		loggerUtil.logSevereAction(ApplicationConstant.EX_METHOD_ARGUMENT_INVALID);
		Map<String, String> errors = new HashMap<>();
		    ex.getBindingResult().getAllErrors().forEach( error -> {
		        String fieldName = ((FieldError) error).getField();
		        String errorMessage = error.getDefaultMessage();
		        errors.put(fieldName, errorMessage);
		    });
		
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				ApplicationConstant.EX_METHOD_ARGUMENT_INVALID,
				ApplicationConstant.EX_METHOD_ARGUMENT_INVALID_DESC,
				errors ),
				HttpStatus.NOT_ACCEPTABLE );
	}
	
	@ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ExceptionResponse>methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){

		loggerUtil.logSevereAction(ApplicationConstant.EX_METHOD_ARGUMENT_TYPE);
		String attrName = ex.getName();
		String typeName = "Unknown type";				
		Class<?> requiredType = ex.getRequiredType();
		
		if ( requiredType != null && requiredType.getName() != null ) typeName = requiredType.getName();
		
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				ApplicationConstant.EX_METHOD_ARGUMENT_TYPE,
				ApplicationConstant.EX_METHOD_ARGUMENT_TYPE_DESC,
				Map.of( attrName, typeName)  ),
				HttpStatus.BAD_REQUEST );
	}	
	
	@ExceptionHandler(
		jakarta.validation.ConstraintViolationException.class
		)		
	public ResponseEntity<ExceptionResponse> jakartaValidationConstraintViolationException(Exception ex){

		loggerUtil.logSevereAction(ApplicationConstant.EX_JAKARTA_VAL);
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				ApplicationConstant.EX_JAKARTA_VAL,
				ApplicationConstant.EX_JAKARTA_VAL_DESC,
				Map.of( ex.getLocalizedMessage(), ex.getMessage())  ),
				HttpStatus.BAD_REQUEST );
	}	
	
	@ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
	public ResponseEntity<ExceptionResponse> noResourceFoundException(Exception ex){
		
		loggerUtil.logSevereAction(ApplicationConstant.EX_NO_RESOURCE);
		return new ResponseEntity<>( new ExceptionResponse( UUID.randomUUID(),
				ApplicationConstant.EX_NO_RESOURCE,
				ApplicationConstant.EX_NO_RESOURCE_DESC,
				Map.of( ex.getLocalizedMessage(), ex.getMessage())  ),
				HttpStatus.BAD_REQUEST );		
	}
	
}
