package com.turingguild.tgms.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DefaultAdminSeeder implements ApplicationRunner {

    private final BootstrapAdminProperties properties;
    private final BootstrapAdminService bootstrapAdminService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("DefaultAdminSeeder triggered.");

        if (!properties.isEnabled()) {
            log.info("Bootstrap admin seeder disabled via configuration.");
            return;
        }

        if (properties.getEmail() == null || properties.getEmail().isBlank()) {
            log.warn("Bootstrap admin email not configured. Seeder skipped.");
            return;
        }

        if (properties.getPassword() == null || properties.getPassword().isBlank()) {
            log.warn("Bootstrap admin password not configured. Seeder skipped.");
            return;
        }

        try {
            bootstrapAdminService.seedDefaultAdmin(
                    properties.getFullName(),
                    properties.getEmail(),
                    properties.getPassword()
            );
        } catch (Exception e) {
            log.error("DefaultAdminSeeder FAILED with exception:", e);
        }

        log.info("Seeder completed.");
    }
}
