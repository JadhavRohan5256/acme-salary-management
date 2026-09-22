package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.entity.Country;
import com.acme.salarymanagement.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {
    @Mock
    private CountryRepository countryRepository;
    @InjectMocks
    private CountryService countryService;
    private Country india;
    private Country usa;

    @BeforeEach
    void beforeEach() {
        india = Country.builder()
                .id(1L)
                .name("India")
                .code("IN")
                .build();

        usa = Country.builder()
                .id(2L)
                .name("United States")
                .code("US")
                .build();
    }

    @Test
    void shouldReturnAllCountries() {
        when(countryRepository.findAll()).thenReturn(List.of(india, usa));
        List<CountryResponse> result = countryService.getAllCountries();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("India", result.get(0).name());
        assertEquals("IN", result.get(0).code());
        assertEquals(2L, result.get(1).id());
        assertEquals("United States", result.get(1).name());
        assertEquals("US", result.get(1).code());
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoCountriesExist() {
        when(countryRepository.findAll()).thenReturn(List.of());
        List<CountryResponse> result = countryService.getAllCountries();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(countryRepository, times(1)).findAll();
    }
}