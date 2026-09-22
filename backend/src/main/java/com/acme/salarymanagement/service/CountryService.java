package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.repository.CountryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<CountryResponse> getAllCountries() {
        return countryRepository.findAll()
            .stream()
            .map(country -> new CountryResponse(
                    country.getId(),
                    country.getName(),
                    country.getCode()
            ))
            .toList();
    }
}