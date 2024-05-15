package com.mobiautobackend.domain.enumeration;

import org.springframework.http.HttpStatus;

public enum ExceptionMessagesEnum {

    //404
    MEMBER_NOT_FOUND(404001, "Member not found for Id informed", HttpStatus.NOT_FOUND),
    DEALERSHIP_NOT_FOUND(404002, "Dealership not found for Id informed", HttpStatus.NOT_FOUND),
    VEHICLE_NOT_FOUND(404003, "Vehicle not found for Id informed", HttpStatus.NOT_FOUND),
    OPPORTUNITY_NOT_FOUND(404003, "Opportunity not found for Id informed", HttpStatus.NOT_FOUND),

    //409
    MEMBER_ALREADY_EXISTS(409001, "Member with these parameters already exists", HttpStatus.CONFLICT),
    DEALERSHIP_ALREADY_EXISTS(409002, "Dealership with these parameters already exists", HttpStatus.CONFLICT),
    OPPORTUNITY_ALREADY_EXISTS(409003, "Opportunity with these parameters already exists", HttpStatus.CONFLICT),

    //403
    NOT_AUTHORIZED(403001, "Not authorized", HttpStatus.FORBIDDEN);


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    ExceptionMessagesEnum(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}