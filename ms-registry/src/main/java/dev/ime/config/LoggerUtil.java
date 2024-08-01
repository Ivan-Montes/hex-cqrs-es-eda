package dev.ime.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

@Component
public class LoggerUtil {

	private final Logger logger;

	public LoggerUtil(Logger logger) {
		super();
		this.logger = logger;
	}	
	
	public void logSevereAction(String msg) {
		
		String logMessage = String.format("### [eXceptionHandler] -> [%s] ###", msg);
		
		logger.log(Level.SEVERE, logMessage);
		
	} 

	public void logInfoAction(String className, String methodName, String msg) {
		
		String logMessage = String.format("### [%s] -> [%s] -> [ %s ]", className, methodName, msg);
		
		logger.log(Level.INFO, logMessage);
		
	}
		
}
