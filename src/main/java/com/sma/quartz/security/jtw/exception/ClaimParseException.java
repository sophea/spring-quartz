package com.sma.quartz.security.jtw.exception;

import com.sma.common.tools.exceptions.BusinessException;

public class ClaimParseException extends BusinessException {
    public ClaimParseException(String message, Integer code) {
        super(message, code);
    }

    public ClaimParseException(String message, Integer errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}
