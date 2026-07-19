package com.turingguild.tgms.startup;

import com.turingguild.tgms.entity.User;
import com.turingguild.tgms.entity.enums.Role;
import com.turingguild.tgms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootstrapAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void seedDefaultAdmin(String fullName, String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();

        long adminCount = userRepository.countByRole(Role.ADMIN);
        log.info("Starting DefaultAdminSeeder...");
        log.info("Admin count = {}", adminCount);

        if (adminCount > 0) {
            log.info("Administrator already exists. Seeder skipped.");
            return;
        }

        if (userRepository.existsByEmail(normalizedEmail)) {
            log.info("Administrator with email {} already exists. Seeder skipped.", normalizedEmail);
            return;
        }

        log.info("Creating default administrator...");

        User admin = User.builder()
                .fullName(fullName != null ? fullName.trim() : "System Administrator")
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .isActive(true)
                .firstLogin(false)
                .build();

        log.info("Saving administrator...");
        User saved = userRepository.save(admin);
        userRepository.flush();

        log.info("Administrator saved successfully with id={}", saved.getId());

        long verifyCount = userRepository.countByRole(Role.ADMIN);
        boolean verifyExists = userRepository.existsByEmail(normalizedEmail);

        log.info("Post-save verification: countByRole(ADMIN)={}, existsByEmail={}", verifyCount, verifyExists);

        if (verifyCount != 1 || !verifyExists) {
            log.error("POST-SAVE VERIFICATION FAILED! count={} exists={}", verifyCount, verifyExists);
        } else {
            log.info("Administrator created successfully.");
        }
    }
}
