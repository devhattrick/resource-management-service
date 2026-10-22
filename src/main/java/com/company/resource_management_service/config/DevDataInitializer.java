package com.company.resource_management_service.config;

import com.company.resource_management_service.developer.DeveloperProfile;
import com.company.resource_management_service.developer.DeveloperProfileRepository;
import com.company.resource_management_service.developer.ProfileStatus;
import com.company.resource_management_service.developer.Seniority;
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

import java.math.BigDecimal;

/**
 * Seeds local test accounts (idempotent): an ADMIN and a DEVELOPER with a profile,
 * so the full change-request flow (dev requests -> admin approves) is testable.
 * Active only under the "local" profile.
 */
@Component
@Profile("local")
public class DevDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataInitializer.class);

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final DeveloperProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDataInitializer(AppUserRepository userRepository,
                              AppRoleRepository roleRepository,
                              DeveloperProfileRepository profileRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedUser("ADMIN001", "admin@local.dev", "Local Admin", "admin123", "ADMIN", false);
        AppUser dev = seedUser("DEV001", "dev@local.dev", "Local Developer", "dev123", "DEVELOPER", true);
        seedProfile(dev);
    }

    private AppUser seedUser(String code, String email, String fullName,
                             String rawPassword, String roleCode, boolean isDeveloper) {
        return userRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
            AppRole role = roleRepository.findByCode(roleCode)
                    .orElseThrow(() -> new IllegalStateException(roleCode + " role missing; check Flyway seed"));
            AppUser user = new AppUser();
            user.setEmployeeCode(code);
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
            user.setActive(true);
            user.getRoles().add(role);
            AppUser saved = userRepository.save(user);
            log.info("[dev-seed] user {} / {}", email, rawPassword);
            return saved;
        });
    }

    private void seedProfile(AppUser dev) {
        if (profileRepository.existsByUserId(dev.getId())) {
            return;
        }
        DeveloperProfile profile = new DeveloperProfile();
        profile.setUser(dev);
        profile.setSeniority(Seniority.SENIOR);
        profile.setTotalExperienceYears(new BigDecimal("5.0"));
        profile.setCapacityPercent(100);
        profile.setProfileStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profile);
        log.info("[dev-seed] developer profile created for {}", dev.getEmail());
    }
}
