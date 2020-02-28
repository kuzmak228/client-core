package com.kuzmak.client.core.utils;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.kuzmak.client.core.exception.ClientException;
import com.kuzmak.client.core.exception.ModelMappingException;
import com.kuzmak.client.core.model.page.ClientPage;
import com.kuzmak.client.core.xml.XmlParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConvertUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER_DESERIALIZE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    public static final DateTimeFormatter DATE_TIME_FORMATTER_SERIALIZE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingStrategy(new CustomStrategy())
            .registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                            ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime())
            .registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, typeOfT, context) ->
                    ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString(), DATE_TIME_FORMATTER_DESERIALIZE))
            .create();

    public static <T> T toSuccessResponse(final CloseableHttpResponse response, final Class<T> clazz, final Gson gson) throws ClientException {
        return checkResponse(response) ? ConvertUtils.toObject(response, clazz, gson) : null;
    }

    public static <T> T toSuccessResponse(final CloseableHttpResponse response, final Class<T> clazz, final XmlParser parser) throws ClientException {
        return checkResponse(response) ? parser.getFirst(XmlParser.toElement(HttpUtils.getResponseContent(response)), clazz) : null;
    }

    public static <T> List<T> toSuccessListResponse(final CloseableHttpResponse response, final Class<T> clazz, final Gson gson) throws ClientException {
        return checkResponse(response) ? toList(clazz, HttpUtils.getResponseContent(response), gson) : null;
    }

    public static <T> List<T> toSuccessListResponse(final CloseableHttpResponse response, final Class<T> clazz, final XmlParser parser) throws ClientException {
        return checkResponse(response) ? parser.getAll(XmlParser.toElement(HttpUtils.getResponseContent(response)), clazz) : null;
    }

    public static <T> ClientPage<T> toSuccessPageResponse(final CloseableHttpResponse response, final Class<T> clazz, final Gson gson) throws ClientException {
        return checkResponse(response) ? toClientPage(clazz, HttpUtils.getResponseContent(response), gson) : null;
    }

    private static boolean checkResponse(final CloseableHttpResponse response) throws ClientException {
        return HttpUtils.STATUS_CODES_SUCCESS.contains(HttpUtils.getStatusCode(response));
    }

    public static <T> T toObject(final CloseableHttpResponse response, final Class<T> clazz, final Gson gson) throws ClientException {
        final var body = HttpUtils.getResponseContent(response);
        if (StringUtils.isEmpty(body)) {
            throw new ModelMappingException("Can't map response to model... " + "Status code: " + HttpUtils.getStatusCode(response));
        }

        return gson.fromJson(body, clazz);
    }

    public static <T> List<T> toList(final Class<T> clazz, final String response, final Gson gson) {
        return gson.fromJson(response, TypeToken.getParameterized(List.class, clazz).getType());
    }

    public static <T> ClientPage<T> toClientPage(final Class<T> clazz, final String response, final Gson gson) {
        return gson.fromJson(response, TypeToken.getParameterized(ClientPage.class, clazz).getType());
    }

    public static <T, R, C> R convert(final CloseableHttpResponse response, final Class<T> clazz,
                                      final C converter, final ConvertFunction<T, R, C> function) throws ClientException {
        try {
            return function.apply(response, clazz, converter);
        } catch (final ClientException e) {
            throw e;
        } catch (final Exception e) {
            throw new ClientException("Can't convert: " + e.getMessage(), e);
        }
    }

    public static class CustomStrategy implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            if (field.getName().equals("errorCodes")) {
                return "error_codes";
            }
            return field.getName();
        }
    }
}
