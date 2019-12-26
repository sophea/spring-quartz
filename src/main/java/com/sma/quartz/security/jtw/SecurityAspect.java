package com.sma.quartz.security.jtw;

import com.sma.common.tools.exceptions.AuthorizationRequiredBusinessException;
import com.sma.quartz.security.jtw.exception.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.sma.quartz.security.jtw.exception.Constants.*;
import static com.sma.quartz.security.jtw.exception.ErrorCode.CORE_001;
import static com.sma.quartz.security.jtw.exception.ErrorMessage.REQUIRED_ROLE_NOT_IN_ACCESS_TOKEN;

@Aspect
public class SecurityAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ClaimsHolder claimsHolder;

    @Around("@annotation(com.sma.quartz.security.jtw.AuthorizationCheck)")
    public Object intercept(final ProceedingJoinPoint joinPoint) throws Throwable {

        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final AuthorizationCheck authorizationCheck = AnnotatedElementUtils.getMergedAnnotation(method, AuthorizationCheck.class);
        final String[] rolesRequired = authorizationCheck != null && authorizationCheck.roles() != null ?
            authorizationCheck.roles() : new String[]{};

        if (rolesRequired.length > 0) {
            final ErrorCode errorCode = (ErrorCode) request.getAttribute(ERROR_CODE);
            if ( errorCode != null && request.getAttribute(ERROR_MESSAGE) != null) {
                throw new AuthorizationRequiredBusinessException((String) request.getAttribute(ERROR_MESSAGE), errorCode.getCode());
            } else {
                final List<String> rolesInToken = claimsHolder.getStringListClaim(CLAIM_ROLES);
                if (rolesInToken.containsAll(Arrays.asList(rolesRequired))) {
                    return joinPoint.proceed(joinPoint.getArgs());
                }
                throw new AuthorizationRequiredBusinessException(REQUIRED_ROLE_NOT_IN_ACCESS_TOKEN, CORE_001.getCode());
            }
        } else {
            return joinPoint.proceed(joinPoint.getArgs());
        }
    }
}
