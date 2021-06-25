package org.sunbird.ruleengine.model;



import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.sunbird.ruleengine.common.NotAuthenticatedException;
import org.sunbird.ruleengine.common.NotAuthorizedException;



@ControllerAdvice
public class ControllerConfig {
	private static final Logger logger = LogManager.getLogger(ControllerConfig.class);
    @ExceptionHandler
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
       // log.warn("Returning HTTP 400 Bad Request", e);
    	e.printStackTrace();
    	logger.error(MarkerFactory.getMarker("Exception") , e);
        throw e;
        
    }
    
    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<Error> notAuthenticated(HttpServletRequest req, Exception e) 
    {
    return new ResponseEntity<Error>(new Error("NOT_LOGGED_IN"), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Error> notAuthorized(HttpServletRequest req, Exception e) 
    {
    return new ResponseEntity<Error>(new Error("NOT_AUTHORIZED"), HttpStatus.UNAUTHORIZED);
    }
    
    class Error{
    	
    	String reason;
    	public Error(String reason){
    		this.reason=reason;
    	}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
    	
    	
    }
    
}