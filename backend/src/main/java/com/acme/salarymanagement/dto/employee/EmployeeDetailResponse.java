package com.acme.salarymanagement.dto.employee;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.dto.currency.CurrencyResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EmployeeDetailResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        CountryResponse country,
        String department,
        String designation,
        BigDecimal currentSalary,
        CurrencyResponse currency,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}