package com.kuzmak.client.core.utils;

import com.kuzmak.client.core.exception.ClientException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpUtils {
    public static final List<Integer> STATUS_CODES_SUCCESS = List.of(200, 201, 204);

    public static List<NameValuePair> asValuePairs(final Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        final var value = field.get(object);
                        return value == null ? null : new BasicNameValuePair(field.getName(), value.toString());
                    } catch (IllegalAccessException ignored) {
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static CloseableHttpClient basicAuthClient(final AuthScope scope, final String login, final String password) {
        final var config = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setConnectionRequestTimeout(300000)
                .setSocketTimeout(300000)
                .build();

        final var builder = HttpClientBuilder.create()
                .setDefaultRequestConfig(config);

        if (!StringUtils.isEmpty(login) && !StringUtils.isEmpty(password)) {
            final var provider = new BasicCredentialsProvider();
            provider.setCredentials(scope, new UsernamePasswordCredentials(login, password));

            builder.setDefaultCredentialsProvider(provider);
        }

        return builder.build();
    }

    public static HttpEntity toJsonEntity(final Object object) throws ClientException {
        try {
            return new StringEntity(ConvertUtils.GSON.toJson(object));
        } catch (Exception e) {
            throw new ClientException("Can't create http entity: " + e.getMessage(), e);
        }
    }

    public static HttpEntity toStringEntity(final String value) throws ClientException {
        try {
            return new StringEntity(value);
        } catch (Exception e) {
            throw new ClientException("Can't create http entity: " + e.getMessage(), e);
        }
    }

    public static Integer getStatusCode(final CloseableHttpResponse response) throws ClientException {
        try {
            return response.getStatusLine().getStatusCode();
        } catch (final Exception e) {
            throw new ClientException("Can't get http status code: " + e.getMessage(), e);
        }
    }

    public static String getResponseContent(final CloseableHttpResponse response) throws ClientException {
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (final Exception e) {
            throw new ClientException("Can't get response content: " + e.getMessage(), e);
        } finally {
            closeResponse(response);
        }
    }

    public static void closeResponse(final CloseableHttpResponse response) {
        try {
            response.close();
        } catch (final IOException ignored) {
            // TODO: normal handle if will be needed
        }
    }
}
