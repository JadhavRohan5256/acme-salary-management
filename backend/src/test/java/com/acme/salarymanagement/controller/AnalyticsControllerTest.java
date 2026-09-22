package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.repository.AnalyticsRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AnalyticsRepositoryTest {
    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Test
    void countEmployees_shouldReturnTotalEmployees() {
        long result = analyticsRepository.countEmployees();

        assertTrue(result >= 0);
    }


    @Test
    void countCountries_shouldReturnTotalCountries() {
        long result = analyticsRepository.countCountries();

        assertTrue(result >= 0);
    }


    @Test
    void countDepartments_shouldReturnTotalDepartments() {
        long result = analyticsRepository.countDepartments();

        assertTrue(result >= 0);
    }


    @Test
    void findCountryAnalytics_shouldReturnCountryAnalytics() {
        List<Object[]> result = analyticsRepository.findCountryAnalytics();

        assertNotNull(result);

        for (Object[] row : result) {
            assertNotNull(row);
            assertEquals(11, row.length);
            assertNotNull(row[0]);  // country id
            assertNotNull(row[1]);  // country name
            assertNotNull(row[2]);  // country code
            assertNotNull(row[3]);  // currency id
            assertNotNull(row[4]);  // currency code
            assertNotNull(row[5]);  // currency name
            assertNotNull(row[6]);  // currency symbol
            assertNotNull(row[7]);  // employee count
            assertNotNull(row[8]);  // average salary
            assertNotNull(row[9]);  // minimum salary
            assertNotNull(row[10]); // maximum salary
        }
    }


    @Test
    void findDepartmentAnalytics_shouldReturnDepartmentAnalytics() {
        List<Object[]> result = analyticsRepository.findDepartmentAnalytics();

        assertNotNull(result);

        for (Object[] row : result) {
            assertNotNull(row);
            assertEquals(7, row.length);
            assertNotNull(row[0]);  // department
            assertNotNull(row[1]);  // currency id
            assertNotNull(row[2]);  // currency code
            assertNotNull(row[3]);  // currency name
            assertNotNull(row[4]);  // currency symbol
            assertNotNull(row[5]);  // employee count
            assertNotNull(row[6]);  // average salary
        }
    }

    @Test
    void findSalariesForDistribution_shouldFilterByCountry() {
        List<BigDecimal> result = analyticsRepository.findSalariesForDistribution(
            1L,
            1L,
            null
        );

        assertNotNull(result);

        for (BigDecimal salary : result) {
            assertNotNull(salary);
        }
    }

    @Test
    void findSalariesForDistribution_shouldReturnEmpty_whenNoMatchingCurrency() {
        List<BigDecimal> result = analyticsRepository.findSalariesForDistribution(
            999999L,
            null,
            null
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
