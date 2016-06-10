package gov.ca.emsa.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gov.ca.emsa.domain.CustomError;

@ControllerAdvice
public class ExceptionAdvice {
	@ExceptionHandler(IOException.class)
	public ResponseEntity<CustomError> exception(IOException e) {
		return new ResponseEntity<CustomError>(new CustomError(e.getMessage()), HttpStatus.NOT_FOUND);
	}
}
