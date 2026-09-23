package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Country;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.entity.Employee;
import com.acme.salarymanagement.entity.SalaryHistory;
import com.acme.salarymanagement.entity.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@DataJpaTest
class SalaryHistoryRepositoryTest {
    @Autowired
    private SalaryHistoryRepository salaryHistoryRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private UserRepository userRepository;
    private Employee employee;
    private Currency currency;
    private User user;

    @BeforeEach
    void beforeEach() {
        Country country = countryRepository.findByCode("IN").orElseThrow();

        currency = currencyRepository.findByCode("INR").orElseThrow();

        user = new User();
        user.setUsername("admin");
        user.setPasswordHash("password");
        user.setRole("ADMIN");
        user = userRepository.save(user);

        employee = Employee.builder()
        	.firstName("John")
            .lastName("Smith")
            .email("john.smith@test.com")
            .country(country)
            .department("Engineering")
            .designation("Software Engineer")
            .currentSalary(new BigDecimal("120000.00"))
            .currency(currency)
            .build();

        employee = employeeRepository.save(employee);

    }

    @Test
    void findByEmployeeIdOrderByEffectiveDateDescIdDesc_shouldReturnSalaryHistoryInDescendingOrder() {
        SalaryHistory oldHistory = SalaryHistory.builder()
            .employee(employee)
            .previousSalary(new BigDecimal("100000.00"))
            .newSalary(new BigDecimal("110000.00"))
            .currency(currency)
            .effectiveDate(LocalDate.of(2026, 1, 1))
            .changedBy(user)
            .build();

        SalaryHistory newHistory = SalaryHistory.builder()
            .employee(employee)
            .previousSalary(new BigDecimal("110000.00"))
            .newSalary(new BigDecimal("120000.00"))
            .currency(currency)
            .effectiveDate(LocalDate.of(2026, 9, 1))
            .changedBy(user)
            .build();

        salaryHistoryRepository.save(oldHistory);
        salaryHistoryRepository.save(newHistory);

        List<SalaryHistory> result = salaryHistoryRepository.findByEmployeeIdOrderByEffectiveDateDescIdDesc(employee.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2026, 9, 1), result.get(0).getEffectiveDate());
        assertEquals(LocalDate.of(2026, 1, 1), result.get(1).getEffectiveDate());
        assertEquals(new BigDecimal("120000.00"), result.get(0).getNewSalary());
        assertEquals(new BigDecimal("110000.00"), result.get(1).getNewSalary());
    }

    @Test
    void findByEmployeeIdOrderByEffectiveDateDescIdDesc_shouldReturnEmptyList_whenEmployeeHasNoHistory() {
        List<SalaryHistory> result = salaryHistoryRepository.findByEmployeeIdOrderByEffectiveDateDescIdDesc(employee.getId());

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void findByEmployeeIdOrderByEffectiveDateDescIdDesc_shouldReturnOnlyRequestedEmployeeHistory() {
        SalaryHistory history = SalaryHistory.builder()
            .employee(employee)
            .previousSalary(new BigDecimal("100000.00"))
            .newSalary(new BigDecimal("120000.00"))
            .currency(currency)
            .effectiveDate(LocalDate.of(2026, 9, 1))
            .changedBy(user)
            .build();

        salaryHistoryRepository.save(history);

        List<SalaryHistory> result = salaryHistoryRepository.findByEmployeeIdOrderByEffectiveDateDescIdDesc(99999L);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

}