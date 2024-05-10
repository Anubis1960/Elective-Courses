package com.Spring.application.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    private static final ThreadLocal<String> auditorName = new ThreadLocal<>();

    public static void setAuditorName(String name) {
        auditorName.set(name);
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        String name = auditorName.get();
        if (name != null) {
            return Optional.of(name);
        } else {
            return Optional.empty();
        }
    }
}
