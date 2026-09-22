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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {
    @Mock
    private CountryRepository countryRepository;
    @InjectMocks
    private CountryService countryService;
    private Country india;
    private Country usa;

    @BeforeEach
    void setUp() {
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

    @Test
    void shouldMapCountryCorrectly() {
        when(countryRepository.findAll()).thenReturn(List.of(india));
        List<CountryResponse> result = countryService.getAllCountries();
        CountryResponse response = result.get(0);

        assertEquals(india.getId(), response.id());
        assertEquals(india.getName(), response.name());
        assertEquals(india.getCode(), response.code());
    }

    @Test
    void shouldCallRepositoryOnlyOnce() {
        when(countryRepository.findAll()).thenReturn(List.of(india));
        countryService.getAllCountries();

        verify(countryRepository, times(1)).findAll();
        verifyNoMoreInteractions(countryRepository);
    }
}