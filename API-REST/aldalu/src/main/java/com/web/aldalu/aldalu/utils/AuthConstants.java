package com.web.aldalu.aldalu.utils;

public class AuthConstants {
    private AuthConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ROLE_CLAIM = "role";

    public static final String CONTENT_TYPE_JSON = "application/json";

}
