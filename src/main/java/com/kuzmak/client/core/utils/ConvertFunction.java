package com.kuzmak.client.core.utils;

import com.kuzmak.client.core.exception.ClientException;
import org.apache.http.client.methods.CloseableHttpResponse;

@FunctionalInterface
public interface ConvertFunction<T, R, C> {
    R apply(CloseableHttpResponse response, Class<T> clazz, C converter) throws ClientException;
}
