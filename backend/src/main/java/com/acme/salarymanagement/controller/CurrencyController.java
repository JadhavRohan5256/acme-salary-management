package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.currency.CurrencyResponse;
import com.acme.salarymanagement.service.CurrencyService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {

        return ResponseEntity.ok(
                currencyService.getAllCurrencies()
        );
    }
}