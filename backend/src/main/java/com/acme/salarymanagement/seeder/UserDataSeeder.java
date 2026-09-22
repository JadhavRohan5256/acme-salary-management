package com.acme.salarymanagement.seeder;

import com.acme.salarymanagement.entity.User;
import com.acme.salarymanagement.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataSeeder(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("hrmanager")) {
            return;
        }

        User user = new User();
        user.setUsername("hrmanager");
        user.setPasswordHash(passwordEncoder.encode("Admin@123"));
        user.setRole("HR_MANAGER");

        userRepository.save(user);
    }
}