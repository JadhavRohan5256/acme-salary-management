package com.acme.salarymanagement.dto.employee;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.dto.currency.CurrencyResponse;

import java.math.BigDecimal;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        CountryResponse country,
        String department,
        String designation,
        BigDecimal currentSalary,
        CurrencyResponse currency
) {}