package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.currency.CurrencyResponse;
import com.acme.salarymanagement.service.CurrencyService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {
    @Mock
    private CurrencyService currencyService;
    
    @InjectMocks
    private CurrencyController currencyController;

    @Test
    void getAllCurrencies_shouldReturnSuccessfulResponse() {
        List<CurrencyResponse> expectedResponse = List.of(
            new CurrencyResponse(1L, "INR", "Indian Rupee", "₹"),
            new CurrencyResponse(2L, "USD", "US Dollar", "$"),
            new CurrencyResponse(3L, "GBP", "British Pound", "£")
        );

        when(currencyService.getAllCurrencies()).thenReturn(expectedResponse);

        ResponseEntity<List<CurrencyResponse>> actualResponse = currencyController.getAllCurrencies();

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertSame(expectedResponse, actualResponse.getBody());

        verify(currencyService).getAllCurrencies();
    }
}