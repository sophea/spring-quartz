package com.sma.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
@ControllerAdvice
@RequestMapping("/error")
public class JsonErrorController implements ErrorController {
    private static final String TRACE = "trace";

    private final ErrorAttributes errorAttributes;

    @Autowired
    public JsonErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping
    public Map<String, Object> error(WebRequest webRequest) {
        Map<String, Object> body = getErrorAttributes(webRequest, true);

        String trace = (String) body.get(TRACE);
        if (trace != null) {
            String[] lines = trace.split("\n\t");
            body.put(TRACE, lines);
        }


        return body;
    }

    private Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        return errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }
}
