package com.acme.salarymanagement.dto.analytics;

public record AnalyticsOverviewResponse(
        long totalEmployees,
        long totalCountries,
        long totalDepartments
) {}