package com.sma.quartz.config;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

//    @Override
//    public Optional<String> getCurrentAuditor() {
//        //  return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT));
//        return Optional.of("Sophea");
//    }

    @Override
    public Optional<String> getCurrentAuditor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername());
        }

        if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        return Optional.of("System");
    }
}
