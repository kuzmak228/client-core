package com.kuzmak.client.core.model;

import com.kuzmak.client.core.exception.ClientException;
import com.kuzmak.client.core.utils.ConvertUtils;
import com.kuzmak.client.core.utils.HttpUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

public class ErrorResponse {

    private String error;
    private int[] errorCodes;
    private long timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(final String error, final long timestamp, final int... errorCodes) {
        this.timestamp = timestamp;
        this.errorCodes = errorCodes;
        this.error = error;
    }

    public static ErrorResponse from(final CloseableHttpResponse response) throws ClientException {
        if (HttpUtils.STATUS_CODES_SUCCESS.contains(HttpUtils.getStatusCode(response))) {
            return null;
        }
        return ConvertUtils.toObject(response, ErrorResponse.class, ConvertUtils.GSON);
    }

    public String getError() {
        return this.error;
    }

    public int[] getErrorCodes() {
        return this.errorCodes;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
