package com.sma.quartz.config;

public final class SwaggerDocumentation {

    public static final String AUTH_HEADER = "Client authentication header starting by bearer ${access_token}";
    public static final String HEADER = "header";
    public static final String AUTHORIZATION = "Authorization";
    public static final String X_AUTHORIZATION = "X-Authorization";
    public static final String AUTH_HEADER_MOBILE = "Client authentication header, for test environment is " +
            "Basic Y2xpZW50SWQ6cGFzc3dvcmQ=";
    private SwaggerDocumentation() {
        //No createOrUpdate
    }
}