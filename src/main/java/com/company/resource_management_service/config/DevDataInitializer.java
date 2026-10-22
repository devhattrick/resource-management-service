package com.company.resource_management_service.config;

import com.company.resource_management_service.user.entity.AppRole;
import com.company.resource_management_service.user.entity.AppUser;
import com.company.resource_management_service.user.repository.AppRoleRepository;
import com.company.resource_management_service.user.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds a single admin account for local development so the login flow is
 * testable. Active only under the "local" profile; no-op if any user exists.
 */
@Component
@Profile("local")
public class DevDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataInitializer.class);

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDataInitializer(AppUserRepository userRepository,
                              AppRoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }
        AppRole adminRole = roleRepository.findByCode("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role missing; check Flyway seed"));

        AppUser admin = new AppUser();
        admin.setEmployeeCode("ADMIN001");
        admin.setEmail("admin@local.dev");
        admin.setFullName("Local Admin");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setActive(true);
        admin.getRoles().add(adminRole);
        userRepository.save(admin);

        log.info("[dev-seed] created admin user: admin@local.dev / admin123");
    }
}
