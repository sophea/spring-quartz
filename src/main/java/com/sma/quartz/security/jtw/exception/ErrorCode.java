package com.sma.quartz.security.jtw.exception;

public enum ErrorCode {
    CORE_001 (1), // required role not in access token
    CORE_002 (2), // authorization header error
    CORE_003 (3), // unspecified error
    CORE_004 (4), // Claim parsing error
    CORE_005(5), // missing mandatory parameter
    CORE_006 (6) , // body is not readable
    F001 (7), // spring framework error
    F002 (8); // unknown error

    private int code;
    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
