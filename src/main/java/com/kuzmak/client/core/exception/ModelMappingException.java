package com.kuzmak.client.core.exception;

public class ModelMappingException extends ClientException {

    public ModelMappingException() {
        super("ModelMappingException");
    }

    public ModelMappingException(final String message) {
        super(message);
    }

    public ModelMappingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
