package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Country;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CountryRepositoryTest {
    @Autowired
    private CountryRepository countryRepository;

    @Test
    void findByCode_shouldReturnCountry_whenCodeExists() {
        Optional<Country> result = countryRepository.findByCode("IN");

        assertTrue(result.isPresent());
        assertEquals("IN", result.get().getCode());
        assertEquals("India", result.get().getName());
    }

    @Test
    void findByCode_shouldReturnEmpty_whenCodeDoesNotExist() {
        Optional<Country> result = countryRepository.findByCode("ZZ");

        assertTrue(result.isEmpty());
    }
}
