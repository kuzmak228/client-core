package com.kuzmak.client.core.exception;

public class ClientException extends Exception {

    public ClientException() {
        super("ClientException");
    }

    public ClientException(final String message) {
        super(message);
    }

    public ClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
