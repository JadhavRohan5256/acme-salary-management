package com.acme.salarymanagement.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record SalaryUpdateRequest(
        @NotNull(message = "Salary is required")
        @DecimalMin(
                value = "0.0",
                message = "Salary must be greater than or equal to zero"
        )
        BigDecimal newSalary,

        @NotNull(message = "Currency is required")
        Long currencyId,

        @NotNull(message = "Effective date is required")
        LocalDate effectiveDate
) {}