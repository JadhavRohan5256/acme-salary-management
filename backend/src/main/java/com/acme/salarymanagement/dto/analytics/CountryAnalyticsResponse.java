package com.acme.salarymanagement.dto.analytics;

import java.math.BigDecimal;

public record CountryAnalyticsResponse(
        CountryInfo country,
        long employeeCount,
        CurrencyInfo currency,
        BigDecimal averageSalary,
        BigDecimal minimumSalary,
        BigDecimal maximumSalary
) {

    public record CountryInfo(
            Long id,
            String name,
            String code
    ) {}

    public record CurrencyInfo(
            Long id,
            String code,
            String name,
            String symbol
    ) {}
}