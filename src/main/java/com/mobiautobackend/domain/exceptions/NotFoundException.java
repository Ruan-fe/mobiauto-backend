package com.mobiautobackend.domain.exceptions;

import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {

	public NotFoundException(final String message) {
		super(message, HttpStatus.NOT_FOUND);
	}

	public NotFoundException(final ExceptionMessagesEnum exceptionMessagesEnum) {
		super(exceptionMessagesEnum.getCode(), exceptionMessagesEnum.getMessage(), HttpStatus.NOT_FOUND);
	}
}