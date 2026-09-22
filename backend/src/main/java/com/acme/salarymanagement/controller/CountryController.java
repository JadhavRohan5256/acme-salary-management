package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }
}