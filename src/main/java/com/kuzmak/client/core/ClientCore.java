package com.kuzmak.client.core;

import com.kuzmak.client.core.exception.AuthenticationException;
import com.kuzmak.client.core.exception.ClientException;
import com.kuzmak.client.core.exception.HttpClientException;
import com.kuzmak.client.core.model.Authentication;
import com.kuzmak.client.core.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class ClientCore {

    protected String apiHost;
    protected CloseableHttpClient httpClient;
    private String clientId;
    private String clientSecret;
    private String authHost;
    private String protocol;
    private Authentication authentication;

    public ClientCore() {
        this.httpClient = createHttpClient();
    }

    public ClientCore(final String clientId, final String clientSecret, final String authHost,
                      final String apiHost, final String protocol) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authHost = authHost;
        this.apiHost = apiHost;
        this.protocol = protocol;
        this.httpClient = createHttpClient();
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
        this.httpClient = createHttpClient();
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        this.httpClient = createHttpClient();
    }

    public String getAuthHost() {
        return authHost;
    }

    public void setAuthHost(String authHost) {
        this.authHost = authHost;
        this.httpClient = createHttpClient();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    protected CloseableHttpResponse sendRequest(final HttpUriRequest request) throws ClientException {
        checkAuthentication(request);
        try {
            return httpClient.execute(request);
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        } finally {
            if (request instanceof HttpRequestBase) {
                ((HttpRequestBase) request).releaseConnection();
            }
        }
    }

    protected URI buildUri(final String host, final String path, final List<NameValuePair> params) throws ClientException {
        try {
            return new URIBuilder()
                    .setScheme(protocol)
                    .setHost(host)
                    .setPath(path)
                    .setParameters(params)
                    .build();
        } catch (URISyntaxException e) {
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    protected CloseableHttpClient createHttpClient() {
        return HttpUtils.basicAuthClient(new AuthScope(authHost, AuthScope.ANY_PORT), clientId, clientSecret);
    }

    protected void checkAuthentication(final HttpUriRequest request) throws ClientException {
        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret)) {
            return;
        }

        if (this.authentication == null || this.authentication.needUpdate()) {
            this.authentication = authenticate();
        }
        request.setHeader("Authorization", "Bearer " + authentication.getAccessToken());
    }

    private Authentication authenticate() throws ClientException {
        final HttpPost request;
        try {
            request = new HttpPost(buildUri(authHost, "/oauth/token", Collections.emptyList()));
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setEntity(new StringEntity("grant_type=client_credentials"));
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        }

        try (final var response = httpClient.execute(request)) {
            final var body = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new AuthenticationException("Can't retrieve auth token...."
                        + " Status code: " + response.getStatusLine().getStatusCode() + " Body: " + body);
            }
            return Authentication.create(body);
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        }
    }
}
