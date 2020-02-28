package com.kuzmak.client.core.model;

import com.google.gson.Gson;
import com.kuzmak.client.core.exception.ClientException;
import com.kuzmak.client.core.model.page.ClientPage;
import com.kuzmak.client.core.utils.ConvertUtils;
import com.kuzmak.client.core.xml.XmlParser;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.List;

public class ClientResponse<T> {

    private T body;
    private ErrorResponse errorResponse;

    public ClientResponse(final T body, final ErrorResponse errorResponse) {
        this.body = body;
        this.errorResponse = errorResponse;
    }

    public static <T> ClientResponse<T> simpleResponse(final Class<T> responseType, final CloseableHttpResponse response) throws ClientException {
        return new ClientResponse<>(ConvertUtils.convert(response, responseType, ConvertUtils.GSON, ConvertUtils::toSuccessResponse), ErrorResponse.from(response));
    }

    public static <T> ClientResponse<T> simpleResponse(final Class<T> responseType, final CloseableHttpResponse response, final Gson gson) throws ClientException {
        return new ClientResponse<>(ConvertUtils.convert(response, responseType, gson, ConvertUtils::toSuccessResponse), ErrorResponse.from(response));
    }

    public static <T> ClientResponse<T> simpleResponse(final Class<T> responseType, final CloseableHttpResponse response, final XmlParser parser) throws ClientException {
        return new ClientResponse<>(ConvertUtils.convert(response, responseType, parser, ConvertUtils::toSuccessResponse), ErrorResponse.from(response));
    }

    public static <T> ClientResponse<T> emptyResponse(final CloseableHttpResponse response) throws ClientException {
        return new ClientResponse<>(null, ErrorResponse.from(response));
    }

    public static <T> ClientResponse<List<T>> listResponse(final Class<T> responseType, final CloseableHttpResponse response) throws ClientException {
        return new ClientResponse<>(ConvertUtils.convert(response, responseType, ConvertUtils.GSON, ConvertUtils::toSuccessListResponse), ErrorResponse.from(response));
    }

    public static <T> ClientResponse<List<T>> listResponse(final Class<T> responseType, final CloseableHttpResponse response, final Gson gson) throws ClientException {
        return new ClientResponse<>(ConvertUtils.convert(response, responseType, gson, ConvertUtils::toSuccessListResponse), ErrorResponse.from(response));
    }

    public static <T> ClientResponse<List<T>> listResponse(final Class<T> responseType, final CloseableHttpResponse response, final XmlParser parser) throws ClientException {
        return new ClientResponse<>(ConvertUtils.convert(response, responseType, parser, ConvertUtils::toSuccessListResponse), ErrorResponse.from(response));
    }

    public static <T> ClientResponse<ClientPage<T>> pageResponse(final Class<T> responseType, final CloseableHttpResponse response) throws ClientException {
        return new ClientResponse<>(ConvertUtils.convert(response, responseType, ConvertUtils.GSON, ConvertUtils::toSuccessPageResponse), ErrorResponse.from(response));
    }

    public final T getResponseEntity() {
        return body;
    }

    public final ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public boolean isErrorResponse() {
        return errorResponse != null;
    }
}
