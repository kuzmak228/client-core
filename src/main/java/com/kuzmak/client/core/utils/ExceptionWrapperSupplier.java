package com.kuzmak.client.core.utils;

import com.kuzmak.client.core.exception.ClientException;

@FunctionalInterface
public interface ExceptionWrapperSupplier<T> {
    T get() throws ClientException;
}
