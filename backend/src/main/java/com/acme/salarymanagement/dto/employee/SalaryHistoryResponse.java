package com.acme.salarymanagement.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.acme.salarymanagement.dto.currency.CurrencyResponse;

public record SalaryHistoryResponse(

        Long id,

        BigDecimal previousSalary,

        BigDecimal newSalary,

        CurrencyResponse currency,

        LocalDate effectiveDate,

        String changedBy,

        LocalDateTime createdAt
) {
}