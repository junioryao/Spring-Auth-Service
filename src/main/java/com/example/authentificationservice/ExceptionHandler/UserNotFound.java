package com.example.authentificationservice.ExceptionHandler;

public class UserNotFound extends Exception{

    public UserNotFound(String message) {
        super(message);
    }
}
