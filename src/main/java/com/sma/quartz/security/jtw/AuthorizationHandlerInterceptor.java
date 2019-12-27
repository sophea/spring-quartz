package com.sma.quartz.security.jtw;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sma.quartz.security.jtw.exception.Constants.*;
import static com.sma.quartz.security.jtw.exception.ErrorCode.CORE_002;
import static com.sma.quartz.security.jtw.exception.ErrorCode.CORE_003;
import static com.sma.quartz.security.jtw.exception.ErrorMessage.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
@Slf4j
public class AuthorizationHandlerInterceptor extends HandlerInterceptorAdapter {

    private JwtSecurityService jwtSecurityService;

    @Autowired
    public void setJwtSecurityService(JwtSecurityService jwtSecurityService) {
        this.jwtSecurityService = jwtSecurityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null) {
            request.setAttribute(ERROR_CODE, CORE_002);
            request.setAttribute(ERROR_MESSAGE, AUTHORIZATION_HEADER_MISSING);
        } else if (authorizationHeader.isEmpty()) {
            request.setAttribute(ERROR_CODE, CORE_002);
            request.setAttribute(ERROR_MESSAGE, AUTHORIZATION_HEADER_EMPTY);
        } else if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            request.setAttribute(ERROR_CODE, CORE_002);
            request.setAttribute(ERROR_MESSAGE, AUTHORIZATION_HEADER_NOT_BEARER_TOKEN);
        } else {
            final String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());
            try {
                request.setAttribute(CLAIMS_SET, jwtSecurityService.getClaimsSetFromToken(accessToken));
            } catch (Exception e) {
               // log.error(e.getMessage(), e);
                request.setAttribute(ERROR_CODE, CORE_003);
                request.setAttribute(ERROR_MESSAGE, "The token is invalid or it is expired");
            }
        }
        return true;
    }
}
