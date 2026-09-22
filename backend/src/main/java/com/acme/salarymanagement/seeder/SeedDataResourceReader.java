package com.acme.salarymanagement.seeder;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class SeedDataResourceReader {
    public List<String> read(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("db/seed/" + fileName);

            return resource.getContentAsString(StandardCharsets.UTF_8)
                .lines()
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .toList();

        } catch (IOException exception) {
            throw new IllegalStateException(
                "Failed to read seed resource: " + fileName,
                exception
            );
        }
    }
}