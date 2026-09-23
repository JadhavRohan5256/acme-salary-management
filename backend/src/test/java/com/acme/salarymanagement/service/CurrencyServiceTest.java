package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.currency.CurrencyResponse;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.repository.CurrencyRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @Mock
    private CurrencyRepository currencyRepository;
    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void getAllCurrencies_shouldReturnCurrencyResponses() {
        Currency inr = Currency.builder()
            .id(1L)
            .code("INR")
            .name("Indian Rupee")
            .symbol("₹")
            .build();

        Currency usd = Currency.builder()
            .id(2L)
            .code("USD")
            .name("US Dollar")
            .symbol("$")
            .build();

        Currency gbp = Currency.builder()
            .id(3L)
            .code("GBP")
            .name("British Pound")
            .symbol("£")
            .build();

        when(currencyRepository.findAll()).thenReturn(List.of(inr, usd, gbp));

        List<CurrencyResponse> response = currencyService.getAllCurrencies();

        assertNotNull(response);
        assertEquals(3, response.size());

        assertEquals(1L, response.get(0).id());
        assertEquals("INR", response.get(0).code());
        assertEquals("Indian Rupee", response.get(0).name());
        assertEquals("₹", response.get(0).symbol());

        assertEquals(2L, response.get(1).id());
        assertEquals("USD", response.get(1).code());
        assertEquals("US Dollar", response.get(1).name());
        assertEquals("$", response.get(1).symbol());

        assertEquals(3L, response.get(2).id());
        assertEquals("GBP", response.get(2).code());
        assertEquals("British Pound", response.get(2).name());
        assertEquals("£", response.get(2).symbol());
        verify(currencyRepository).findAll();
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    void getAllCurrencies_shouldReturnEmptyListWhenNoCurrenciesExist() {
        when(currencyRepository.findAll()).thenReturn(List.of());
        List<CurrencyResponse> response = currencyService.getAllCurrencies();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(currencyRepository).findAll();
        verifyNoMoreInteractions(currencyRepository);
    }
}