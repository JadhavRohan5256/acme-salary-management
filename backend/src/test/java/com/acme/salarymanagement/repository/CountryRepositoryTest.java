package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Country;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void findByCode_shouldReturnCorrectCountry_whenMultipleCountriesExist() {
        Optional<Country> india = countryRepository.findByCode("IN");
        Optional<Country> usa = countryRepository.findByCode("US");
        Optional<Country> germany = countryRepository.findByCode("DE");

        assertTrue(india.isPresent());
        assertTrue(usa.isPresent());
        assertTrue(germany.isPresent());

        assertEquals("IN", india.get().getCode());
        assertEquals("India", india.get().getName());

        assertEquals("US", usa.get().getCode());
        assertEquals("United States", usa.get().getName());

        assertEquals("DE", germany.get().getCode());
        assertEquals("Germany", germany.get().getName());
    }
}
