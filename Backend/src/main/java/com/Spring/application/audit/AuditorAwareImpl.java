package com.Spring.application.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    private static String auditorName;

    public static void setAuditorName(String name) {
        //System.out.println("Setting auditor name: " + name);
        auditorName = name;
    }

    public static String getAuditorName() {
        return auditorName;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        String name = getAuditorName();
        //System.out.println("Current auditor: " + name);
        if (name != null) {
            return Optional.of(name);
        } else {
            return Optional.empty();
        }
    }
}
