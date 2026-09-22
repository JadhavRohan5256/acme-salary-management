package com.acme.salarymanagement.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryUpdateRequest(
        BigDecimal newSalary,
        Long currencyId,
        LocalDate effectiveDate
) {}