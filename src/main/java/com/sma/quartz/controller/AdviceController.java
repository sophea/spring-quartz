package com.sma.quartz.controller;


import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mak Sophea
 * @date : 12/17/2019
 **/
@ControllerAdvice
public class AdviceController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(new MyDateFormat(), true));
    }

    private static final class MyDateFormat extends DateFormat {

        //Tue, 15 Nov 1994 12:45:26 GMT
        private final SimpleDateFormat defaultFormat;
        private static final List<? extends DateFormat> DATE_FORMATTERS = Arrays.asList(

                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

        MyDateFormat() {
            defaultFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.getDefault());
            defaultFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }

        @Override
        public StringBuffer format(final Date date, final StringBuffer toAppendTo, final FieldPosition fieldPosition) {
            throw new UnsupportedOperationException("This custom date formatter can only be used to *parse* Dates.");
        }

        @Override
        public Date parse(final String source, final ParsePosition pos) {
            //check with default Format
            Date res = defaultFormat.parse(source, pos);
            if (res != null) {
                return res;
            }

            for (final DateFormat dateFormat : DATE_FORMATTERS) {
                dateFormat.setLenient(false);
                res = dateFormat.parse(source, pos);
                if (res != null) {
                    return res;
                }
            }
            return null;
        }
    }
}