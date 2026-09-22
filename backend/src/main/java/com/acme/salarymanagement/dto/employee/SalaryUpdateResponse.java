package com.acme.salarymanagement.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.acme.salarymanagement.dto.currency.CurrencyResponse;

public record SalaryUpdateResponse(
        Long employeeId,
        BigDecimal previousSalary,
        BigDecimal newSalary,
        CurrencyResponse currency,
        LocalDate effectiveDate,
        String updatedBy,
        LocalDateTime updatedAt
) {}