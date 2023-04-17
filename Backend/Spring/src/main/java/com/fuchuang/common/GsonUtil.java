package com.fuchuang.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.nio.charset.Charset;


public class GsonUtil {
    private final static Gson gson = new Gson();

    private final static Gson disableHtmlEscapingGson = new GsonBuilder().disableHtmlEscaping().create();

    public static <T> T fromJson(String value, Class<T> type) {
        return gson.fromJson(value, type);
    }

    public static <T> T fromJson(String value, Type type) {
        return gson.fromJson(value, type);
    }

    public static String toJson(Object value) {
        return gson.toJson(value);
    }

    public static String toJsonDisableHtmlEscaping(Object value) {
        return disableHtmlEscapingGson.toJson(value);
    }

}