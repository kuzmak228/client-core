package com.kuzmak.client.core.exception;

public class AuthenticationException extends ClientException {

    public AuthenticationException() {
        super("AuthenticationException");
    }

    public AuthenticationException(final String message) {
        super(message);
    }

    public AuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
