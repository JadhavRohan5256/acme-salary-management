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

    @Test
    void findByCode_shouldReturnCorrectCurrency_whenMultipleCurrenciesExist() {
        Optional<Currency> inr = currencyRepository.findByCode("INR");
        Optional<Currency> usd = currencyRepository.findByCode("USD");
        Optional<Currency> eur = currencyRepository.findByCode("EUR");

        assertTrue(inr.isPresent());
        assertTrue(usd.isPresent());
        assertTrue(eur.isPresent());

        assertEquals("INR", inr.get().getCode());
        assertEquals("Indian Rupee", inr.get().getName());
        assertEquals("₹", inr.get().getSymbol());

        assertEquals("USD", usd.get().getCode());
        assertEquals("US Dollar", usd.get().getName());
        assertEquals("$", usd.get().getSymbol());

        assertEquals("EUR", eur.get().getCode());
        assertEquals("Euro", eur.get().getName());
        assertEquals("€", eur.get().getSymbol());
    }
}
