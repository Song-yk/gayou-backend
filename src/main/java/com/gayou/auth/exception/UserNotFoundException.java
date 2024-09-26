package com.gayou.auth.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message); // 메시지를 부모 클래스에 전달
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause); // 메시지와 원인 예외를 부모 클래스에 전달
    }
}
