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
            createUser("hrmanager", "Admin@123", "HR_MANAGER");
            createUser("acmemanager", "Admin@12345", "HR_MANAGER");
        }

        private void createUser(String username, String password, String role) {
            if (userRepository.existsByUsername(username)) {
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setRole(role);

            userRepository.save(user);
        }
    }