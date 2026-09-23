package com.acme.salarymanagement.dto.currency;

public record CurrencyResponse(
        Long id,
        String code,
        String name,
        String symbol
) {}