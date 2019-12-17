package com.sma.quartz.config;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("PMD.LongVariable")
public class SecurityRoles {

    private static final String ROLE = "ROLE_";

    public static final String ACTUATOR = "ACTUATOR";
    public static final String ROLE_ACTUATOR = ROLE + ACTUATOR;

    public static final String BACKOFFICE_ADMIN = "BACKOFFICE_ADMIN";
    public static final String ROLE_BACKOFFICE_ADMIN = ROLE + BACKOFFICE_ADMIN;

    public static final String MOBILE_CLIENT = "MOBILE_CLIENT";
    public static final String ROLE_MOBILE_CLIENT = ROLE + MOBILE_CLIENT;

}
