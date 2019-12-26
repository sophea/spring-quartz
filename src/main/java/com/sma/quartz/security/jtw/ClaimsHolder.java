package com.sma.quartz.security.jtw;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sma.quartz.security.jtw.exception.ClaimParseException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import static com.sma.quartz.security.jtw.exception.Constants.CLAIMS_SET;
import static com.sma.quartz.security.jtw.exception.ErrorCode.CORE_004;

public class ClaimsHolder {

    @Autowired
    HttpServletRequest request;

    public List<String> getStringListClaim(final String name) {
        try {
            if (request.getAttribute(CLAIMS_SET) != null) {
                return notNullStringList(((JWTClaimsSet) request.getAttribute(CLAIMS_SET)).getStringListClaim(name));
            } else {
                return Collections.emptyList();
            }
        } catch (ParseException e) {
            throw new ClaimParseException(e.getMessage(), CORE_004.getCode(), e);
        }
    }

    private List<String> notNullStringList(List<String> objectList) {
        return objectList != null ? objectList : Collections.emptyList();
    }
}
