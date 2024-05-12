package com.mobiautobackend.domain.exceptions;

import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import org.springframework.http.HttpStatus;

import java.net.URI;

public class ConflictException extends HttpException {

    private URI location;

    public ConflictException(final String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException(final ExceptionMessagesEnum exceptionMessagesEnum, final URI locationURI) {
        super(exceptionMessagesEnum.getCode(), exceptionMessagesEnum.getMessage(), HttpStatus.CONFLICT);
        this.location = locationURI;
    }

    public URI getLocation() {
        return location;
    }
}