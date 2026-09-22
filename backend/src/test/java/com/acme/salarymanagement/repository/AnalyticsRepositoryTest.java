package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Country;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.entity.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnalyticsRepositoryTest {
    @Autowired
    private AnalyticsRepository analyticsRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    private Country india;
    private Country usa;
    private Currency inr;
    private Currency usd;

    @BeforeEach
    void setUp() {
        analyticsRepository.deleteAll();

        inr = currencyRepository.findAll()
                .stream()
                .filter(currency -> "INR".equals(currency.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("INR currency not found"));

        usd = currencyRepository.findAll()
                .stream()
                .filter(currency -> "USD".equals(currency.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("USD currency not found"));

        india = countryRepository.findAll()
                .stream()
                .filter(country -> "IN".equals(country.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("India country not found"));

        usa = countryRepository.findAll()
                .stream()
                .filter(country -> "US".equals(country.getCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("USA country not found"));

        Employee employee1 = createEmployee(
                "Rohan",
                "Jadhav",
                "Engineering",
                new BigDecimal("80000.00"),
                india,
                inr
        );

        Employee employee2 = createEmployee(
                "Amit",
                "Patil",
                "Engineering",
                new BigDecimal("100000.00"),
                india,
                inr
        );

        Employee employee3 = createEmployee(
                "John",
                "Smith",
                "Engineering",
                new BigDecimal("6000.00"),
                usa,
                usd
        );

        Employee employee4 = createEmployee(
                "David",
                "Brown",
                "HR",
                new BigDecimal("5000.00"),
                usa,
                usd
        );

        analyticsRepository.saveAll(
                List.of(
                        employee1,
                        employee2,
                        employee3,
                        employee4
                )
        );
    }

    private Employee createEmployee(
            String firstName,
            String lastName,
            String department,
            BigDecimal salary,
            Country country,
            Currency currency
    ) {

        Employee employee = new Employee();

        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartment(department);
        employee.setDesignation("Software Engineer");
        employee.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
        employee.setCurrentSalary(salary);
        employee.setCountry(country);
        employee.setCurrency(currency);

        return employee;
    }

    @Test
    void shouldCountEmployees() {
        long count = analyticsRepository.countEmployees();

        assertEquals(4L, count);
    }

    @Test
    void shouldCountCountries() {
        long count = analyticsRepository.countCountries();

        assertEquals(2L, count);
    }

    @Test
    void shouldCountDepartments() {
        long count = analyticsRepository.countDepartments();

        assertEquals(2L, count);
    }

    @Test
    void shouldFindCountryAnalytics() {
        List<Object[]> results = analyticsRepository.findCountryAnalytics();

        assertNotNull(results);
        assertEquals(2, results.size());

        Object[] indiaRow = results.get(0);

        assertEquals(india.getId(), ((Number) indiaRow[0]).longValue());
        assertEquals("India", indiaRow[1]);
        assertEquals("IN", indiaRow[2]);
        assertEquals(inr.getId(), ((Number) indiaRow[3]).longValue());
        assertEquals("INR", indiaRow[4]);
        assertEquals("Indian Rupee", indiaRow[5]);
        assertEquals("₹", indiaRow[6]);

        assertEquals(2L, ((Number) indiaRow[7]).longValue());

        Double indiaAverage = ((Number) indiaRow[8]).doubleValue();

        assertEquals( 90000.0, indiaAverage, 0.001);
        assertEquals(0, ((BigDecimal) indiaRow[9]).compareTo(new BigDecimal("80000.00")));
        assertEquals(0, ((BigDecimal) indiaRow[10]).compareTo(new BigDecimal("100000.00")));


        Object[] usaRow = results.get(1);

        assertEquals(usa.getId(), ((Number) usaRow[0]).longValue());
        assertEquals("United States", usaRow[1]);
        assertEquals("US", usaRow[2]);
        assertEquals(usd.getId(), ((Number) usaRow[3]).longValue());
        assertEquals("USD", usaRow[4]);
        assertEquals("US Dollar", usaRow[5]);
        assertEquals("$", usaRow[6]);
        assertEquals(2L, ((Number) usaRow[7]).longValue());

        Double usaAverage = ((Number) usaRow[8]).doubleValue();

        assertEquals(5500.0, usaAverage, 0.001);
        assertEquals(0, ((BigDecimal) usaRow[9]).compareTo(new BigDecimal("5000.00")));
        assertEquals(0, ((BigDecimal) usaRow[10]).compareTo(new BigDecimal("6000.00")));
    }

    @Test
    void shouldFindDepartmentAnalytics() {
        List<Object[]> results = analyticsRepository.findDepartmentAnalytics();

        assertNotNull(results);
        assertEquals(3, results.size());

        Object[] engineeringInr = results.get(0);

        assertEquals("Engineering", engineeringInr[0]);
        assertEquals(inr.getId(), ((Number) engineeringInr[1]).longValue());
        assertEquals("INR", engineeringInr[2]);
        assertEquals("Indian Rupee", engineeringInr[3]);
        assertEquals("₹", engineeringInr[4]);
        assertEquals(2L, ((Number) engineeringInr[5]).longValue());

        Double engineeringInrAverage = ((Number) engineeringInr[6]).doubleValue();

        assertEquals(90000.0, engineeringInrAverage, 0.001);

        Object[] engineeringUsd = results.get(1);

        assertEquals("Engineering", engineeringUsd[0]);
        assertEquals(usd.getId(), ((Number) engineeringUsd[1]).longValue());
        assertEquals("USD", engineeringUsd[2]);
        assertEquals("US Dollar", engineeringUsd[3]);
        assertEquals("$", engineeringUsd[4]);
        assertEquals(1L, ((Number) engineeringUsd[5]).longValue());

        Double engineeringUsdAverage = ((Number) engineeringUsd[6]).doubleValue();

        assertEquals(6000.0, engineeringUsdAverage, 0.001);

        Object[] hrUsd = results.get(2);

        assertEquals("HR", hrUsd[0]);
        assertEquals(usd.getId(), ((Number) hrUsd[1]).longValue());
        assertEquals("USD", hrUsd[2]);
        assertEquals("US Dollar", hrUsd[3]);
        assertEquals("$", hrUsd[4]);
        assertEquals(1L, ((Number) hrUsd[5]).longValue());

        Double hrUsdAverage = ((Number) hrUsd[6]).doubleValue();

        assertEquals(5000.0, hrUsdAverage, 0.001);
    }

    @Test
    void shouldFindSalariesForDistributionByCurrency() {
        List<BigDecimal> salaries = analyticsRepository.findSalariesForDistribution(
                inr.getId(),
                null,
                null
        );

        assertNotNull(salaries);
        assertEquals(2, salaries.size());
        assertTrue(salaries.stream().anyMatch(salary -> salary.compareTo(new BigDecimal("80000.00")) == 0));
        assertTrue(salaries.stream().anyMatch(salary -> salary.compareTo(new BigDecimal("100000.00")) == 0));
    }

    @Test
    void shouldFindSalariesForDistributionByCurrencyAndCountry() {
        List<BigDecimal> salaries = analyticsRepository.findSalariesForDistribution(
                inr.getId(),
                india.getId(),
                null
        );

        assertNotNull(salaries);
        assertEquals(2, salaries.size());
        assertTrue(salaries.stream().anyMatch(salary -> salary.compareTo(new BigDecimal("80000.00")) == 0));
        assertTrue(salaries.stream().anyMatch(salary -> salary.compareTo(new BigDecimal("100000.00")) == 0));
    }

    @Test
    void shouldFindSalariesForDistributionByCurrencyAndDepartment() {
        List<BigDecimal> salaries = analyticsRepository.findSalariesForDistribution(
                usd.getId(),
                null,
                "HR"
        );

        assertNotNull(salaries);
        assertEquals(1, salaries.size());
        assertEquals(0, salaries.get(0).compareTo(new BigDecimal("5000.00")));
    }

    @Test
    void shouldFindSalariesForDistributionByAllFilters() {
        List<BigDecimal> salaries = analyticsRepository.findSalariesForDistribution(
                inr.getId(),
                india.getId(),
                "Engineering"
        );

        assertNotNull(salaries);
        assertEquals(2, salaries.size());
        assertTrue(salaries.stream().anyMatch(salary -> salary.compareTo(new BigDecimal("80000.00")) == 0));
        assertTrue(salaries.stream().anyMatch(salary -> salary.compareTo(new BigDecimal("100000.00")) == 0));
    }

    @Test
    void shouldReturnEmptySalariesWhenCurrencyAndCountryDoNotMatch() {
        List<BigDecimal> salaries = analyticsRepository.findSalariesForDistribution(
                inr.getId(),
                usa.getId(),
                null
        );

        assertNotNull(salaries);
        assertTrue(salaries.isEmpty());
    }
}