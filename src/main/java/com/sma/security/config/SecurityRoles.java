package com.sma.security.config;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("PMD.LongVariable")
public class SecurityRoles {

    private static final String ROLE = "ROLE_";

    public static final String ROLE_ACTUATOR = ROLE + "ACTUATOR";

    public static final String ROLE_BACKOFFICE_ADMIN = ROLE + "BACKOFFICE_ADMIN";

    public static final String ROLE_USER = ROLE + "USER";

    public static final String ROLE_MOBILE_CLIENT = ROLE + "MOBILE_CLIENT";

}
