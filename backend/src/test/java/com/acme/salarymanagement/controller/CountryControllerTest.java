package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.service.CountryService;

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
class CountryControllerTest {
    @Mock
    private CountryService countryService;
    
    @InjectMocks
    private CountryController countryController;

    @Test
    void getAllCountries_shouldReturnSuccessfulResponse() {
        List<CountryResponse> expectedResponse = List.of(
            new CountryResponse(1L, "India", "IN"),
            new CountryResponse(2L, "United States", "US"),
            new CountryResponse(3L, "United Kingdom", "GB")
        );

        when(this.countryService.getAllCountries()).thenReturn(expectedResponse);

        ResponseEntity<List<CountryResponse>> actualResponse = this.countryController.getAllCountries();

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertSame(expectedResponse, actualResponse.getBody());
        verify(this.countryService).getAllCountries();
    }
}