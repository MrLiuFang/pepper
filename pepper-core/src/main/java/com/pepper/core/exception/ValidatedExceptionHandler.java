package com.pepper.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 
 * @author mrliu
 *
 */
@ControllerAdvice
public class ValidatedExceptionHandler{

	@ExceptionHandler(value = {Exception.class})
	public void validHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
		if(exception instanceof MethodArgumentNotValidException){
			for (FieldError error : ((MethodArgumentNotValidException)exception).getBindingResult().getFieldErrors()) {
				error.getDefaultMessage();
			}
		}
		if(exception instanceof BindException){
			for (FieldError error : ((BindException)exception).getBindingResult().getFieldErrors()) {
				error.getDefaultMessage();
			}
		}
		else{
			
		}
	}
}
