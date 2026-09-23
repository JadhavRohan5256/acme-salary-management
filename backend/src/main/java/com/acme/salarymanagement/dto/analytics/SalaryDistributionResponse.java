package com.acme.salarymanagement.dto.analytics;

public record SalaryDistributionResponse(
        String range,
        long employeeCount
) {}