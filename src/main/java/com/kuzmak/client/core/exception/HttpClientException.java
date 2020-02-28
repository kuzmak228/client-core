package com.kuzmak.client.core.exception;

public class HttpClientException extends ClientException {

    public HttpClientException() {
        super("HttpClientException");
    }

    public HttpClientException(final String message) {
        super(message);
    }

    public HttpClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
