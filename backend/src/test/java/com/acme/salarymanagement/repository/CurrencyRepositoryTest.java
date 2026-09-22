package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CurrencyRepositoryTest {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void findByCode_shouldReturnCurrency_whenCodeExists() {
        Optional<Currency> result = currencyRepository.findByCode("INR");

        assertTrue(result.isPresent());
        assertEquals("INR", result.get().getCode());
        assertEquals("Indian Rupee", result.get().getName());
        assertEquals("₹", result.get().getSymbol());
    }

    @Test
    void findByCode_shouldReturnEmpty_whenCodeDoesNotExist() {
        Optional<Currency> result = currencyRepository.findByCode("XYZ");

        assertTrue(result.isEmpty());
    }
}
