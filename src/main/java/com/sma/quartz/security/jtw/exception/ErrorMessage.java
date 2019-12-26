package com.sma.quartz.security.jtw.exception;

public class ErrorMessage {
    public static final String REQUIRED_ROLE_NOT_IN_ACCESS_TOKEN = "Required role is not in the access token.";
    public static final String AUTHORIZATION_HEADER_MISSING = "Authorization header is missing.";
    public static final String AUTHORIZATION_HEADER_EMPTY = "Authorization header is empty.";
    public static final String AUTHORIZATION_HEADER_NOT_BEARER_TOKEN = "Authorization header is not a Bearer token.";
    public static final String SYSTEM_ERROR = "System error";
    public static final String REQUEST_ERROR = "Request error";
}
