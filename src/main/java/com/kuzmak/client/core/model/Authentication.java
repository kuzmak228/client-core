package com.kuzmak.client.core.model;

import com.google.gson.JsonObject;
import com.kuzmak.client.core.utils.ConvertUtils;

import java.time.LocalDateTime;


/**
 * Authentication for using api
 */
public class Authentication {

    private String accessToken;
    private LocalDateTime expiresAt;

    public static Authentication create(final String from) {

        final var jsonObject = ConvertUtils.GSON.fromJson(from, JsonObject.class);
        final var expiresIn = jsonObject.get("expires_in").getAsInt();

        final var authentication = new Authentication();
        authentication.setAccessToken(jsonObject.get("access_token").getAsString());
        authentication.setExpiresAt(LocalDateTime.now().plusSeconds((long) (expiresIn * 0.9)));

        return authentication;
    }

    public boolean needUpdate() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void setExpiresAt(final LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
