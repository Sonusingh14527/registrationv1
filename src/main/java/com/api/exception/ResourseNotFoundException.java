package com.api.exception;

public class ResourseNotFoundException extends RuntimeException{

    public ResourseNotFoundException(String msg) {
        super(msg);
    }
}
