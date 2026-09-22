package com.acme.salarymanagement.dto.analytics;

import java.math.BigDecimal;

public record DepartmentAnalyticsResponse(
        String department,
        CurrencyInfo currency,
        long employeeCount,
        BigDecimal averageSalary
) {

    public record CurrencyInfo(
            Long id,
            String code,
            String name,
            String symbol
    ) {}
}