package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.analytics.AnalyticsOverviewResponse;
import com.acme.salarymanagement.dto.analytics.CountryAnalyticsResponse;
import com.acme.salarymanagement.dto.analytics.DepartmentAnalyticsResponse;
import com.acme.salarymanagement.dto.analytics.SalaryDistributionResponse;
import com.acme.salarymanagement.repository.AnalyticsRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {
    @Mock
    private AnalyticsRepository analyticsRepository;
    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void shouldReturnAnalyticsOverview() {

        when(analyticsRepository.countEmployees()).thenReturn(100L);
        when(analyticsRepository.countCountries()).thenReturn(5L);
        when(analyticsRepository.countDepartments()).thenReturn(8L);

        AnalyticsOverviewResponse result = analyticsService.getOverview();

        assertNotNull(result);
        assertEquals(100L, result.totalEmployees());
        assertEquals(5L, result.totalCountries());
        assertEquals(8L, result.totalDepartments());

        verify(analyticsRepository, times(1)).countEmployees();
        verify(analyticsRepository, times(1)).countCountries();
        verify(analyticsRepository, times(1)).countDepartments();
    }

    @Test
    void shouldReturnZeroWhenNoAnalyticsDataExists() {
        when(analyticsRepository.countEmployees()).thenReturn(0L);
        when(analyticsRepository.countCountries()).thenReturn(0L);
        when(analyticsRepository.countDepartments()).thenReturn(0L);

        AnalyticsOverviewResponse result = analyticsService.getOverview();

        assertNotNull(result);
        assertEquals(0L, result.totalEmployees());
        assertEquals(0L, result.totalCountries());
        assertEquals(0L, result.totalDepartments());

        verify(analyticsRepository, times(1)).countEmployees();
        verify(analyticsRepository, times(1)).countCountries();
        verify(analyticsRepository, times(1)).countDepartments();
    }

    @Test
    void shouldReturnCountryAnalytics() {
        List<Object[]> countryRows = List.<Object[]>of(
                new Object[]{
                        1L,
                        "India",
                        "IN",
                        1L,
                        "INR",
                        "Indian Rupee",
                        "₹",
                        10L,
                        75000.0,
                        new BigDecimal("40000.00"),
                        new BigDecimal("120000.00")
                },
                new Object[]{
                        2L,
                        "United States",
                        "US",
                        2L,
                        "USD",
                        "US Dollar",
                        "$",
                        5L,
                        90000.0,
                        new BigDecimal("50000.00"),
                        new BigDecimal("150000.00")
                }
        );

        when(analyticsRepository.findCountryAnalytics()).thenReturn(countryRows);

        List<CountryAnalyticsResponse> result = analyticsService.getByCountry();

        assertNotNull(result);
        assertEquals(2, result.size());

        CountryAnalyticsResponse india = result.get(0);

        assertNotNull(india.country());
        assertEquals(1L, india.country().id());
        assertEquals("India", india.country().name());
        assertEquals("IN", india.country().code());
        assertEquals(10L, india.employeeCount());

        assertNotNull(india.currency());
        assertEquals(1L, india.currency().id());
        assertEquals("INR", india.currency().code());
        assertEquals("Indian Rupee", india.currency().name());
        assertEquals("₹", india.currency().symbol());

        assertEquals(new BigDecimal("75000.00"), india.averageSalary());
        assertEquals(new BigDecimal("40000.00"), india.minimumSalary());
        assertEquals(new BigDecimal("120000.00"), india.maximumSalary());

        CountryAnalyticsResponse usa = result.get(1);

        assertNotNull(usa.country());
        assertEquals(2L, usa.country().id());
        assertEquals("United States", usa.country().name());
        assertEquals("US", usa.country().code());
        assertEquals(5L, usa.employeeCount());

        assertNotNull(usa.currency());
        assertEquals(2L, usa.currency().id());
        assertEquals("USD", usa.currency().code());
        assertEquals("US Dollar", usa.currency().name());
        assertEquals("$", usa.currency().symbol());

        assertEquals(new BigDecimal("90000.00"), usa.averageSalary());
        assertEquals(new BigDecimal("50000.00"), usa.minimumSalary());
        assertEquals(new BigDecimal("150000.00"), usa.maximumSalary());

        verify(analyticsRepository, times(1)).findCountryAnalytics();
    }

    @Test
    void shouldReturnEmptyListWhenNoCountryAnalyticsExists() {
        List<Object[]> countryRows = List.<Object[]>of();

        when(analyticsRepository.findCountryAnalytics()).thenReturn(countryRows);

        List<CountryAnalyticsResponse> result = analyticsService.getByCountry();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(analyticsRepository, times(1)).findCountryAnalytics();
    }

    @Test
    void shouldReturnDepartmentAnalytics() {
        List<Object[]> departmentRows = List.<Object[]>of(
                new Object[]{
                        "Engineering",
                        1L,
                        "INR",
                        "Indian Rupee",
                        "₹",
                        10L,
                        85000.0
                },
                new Object[]{
                        "Human Resources",
                        1L,
                        "INR",
                        "Indian Rupee",
                        "₹",
                        5L,
                        65000.0
                }
        );

        when(analyticsRepository.findDepartmentAnalytics()).thenReturn(departmentRows);

        List<DepartmentAnalyticsResponse> result = analyticsService.getByDepartment();

        assertNotNull(result);
        assertEquals(2, result.size());

        DepartmentAnalyticsResponse engineering = result.get(0);

        assertEquals("Engineering", engineering.department());
        assertNotNull(engineering.currency());
        assertEquals(1L, engineering.currency().id());
        assertEquals("INR", engineering.currency().code());
        assertEquals("Indian Rupee", engineering.currency().name());
        assertEquals("₹", engineering.currency().symbol());
        assertEquals(10L, engineering.employeeCount());
        assertEquals(new BigDecimal("85000.00"), engineering.averageSalary());

        DepartmentAnalyticsResponse hr = result.get(1);

        assertEquals("Human Resources", hr.department());
        assertEquals(5L, hr.employeeCount());
        assertEquals(new BigDecimal("65000.00"), hr.averageSalary());

        verify(analyticsRepository, times(1)).findDepartmentAnalytics();
    }

    @Test
    void shouldReturnEmptyListWhenNoDepartmentAnalyticsExists() {
        List<Object[]> departmentRows = List.<Object[]>of();

        when(analyticsRepository.findDepartmentAnalytics()).thenReturn(departmentRows);

        List<DepartmentAnalyticsResponse> result = analyticsService.getByDepartment();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(analyticsRepository, times(1)).findDepartmentAnalytics();
    }

    @Test
    void shouldCalculateSalaryDistributionCorrectly() {
        List<BigDecimal> salaries = List.of(
                new BigDecimal("25000"),
                new BigDecimal("50000"),
                new BigDecimal("50001"),
                new BigDecimal("100000"),
                new BigDecimal("100001"),
                new BigDecimal("150000"),
                new BigDecimal("150001"),
                new BigDecimal("200000"),
                new BigDecimal("200001")
        );

        when(analyticsRepository.findSalariesForDistribution(
                1L,
                null,
                null
        )).thenReturn(salaries);

        List<SalaryDistributionResponse> result = analyticsService.getSalaryDistribution(
                1L,
                null,
                null
        );

        assertNotNull(result);
        assertEquals(5, result.size());

        assertEquals("0-50000", result.get(0).range());
        assertEquals(2L, result.get(0).employeeCount());

        assertEquals("50001-100000", result.get(1).range());
        assertEquals(2L, result.get(1).employeeCount());

        assertEquals("100001-150000", result.get(2).range());
        assertEquals(2L, result.get(2).employeeCount());

        assertEquals("150001-200000", result.get(3).range());
        assertEquals(2L, result.get(3).employeeCount());

        assertEquals("200000+", result.get(4).range());
        assertEquals(1L, result.get(4).employeeCount());

        verify(analyticsRepository, times(1)).findSalariesForDistribution(
                1L,
                null,
                null
        );
    }

    @Test
    void shouldIncludeBoundarySalaryValuesInCorrectRanges() {
        List<BigDecimal> salaries = List.of(
                new BigDecimal("50000"),
                new BigDecimal("50001"),
                new BigDecimal("100000"),
                new BigDecimal("100001"),
                new BigDecimal("150000"),
                new BigDecimal("150001"),
                new BigDecimal("200000"),
                new BigDecimal("200001")
        );

        when(analyticsRepository.findSalariesForDistribution(
                1L,
                null,
                null
        )).thenReturn(salaries);

        List<SalaryDistributionResponse> result = analyticsService.getSalaryDistribution(
                1L,
                null,
                null
        );

        assertNotNull(result);
        assertEquals(5, result.size());

        assertEquals(1L, result.get(0).employeeCount());
        assertEquals(2L, result.get(1).employeeCount());
        assertEquals(2L, result.get(2).employeeCount());
        assertEquals(2L, result.get(3).employeeCount());
        assertEquals(1L, result.get(4).employeeCount());

        verify(analyticsRepository, times(1)).findSalariesForDistribution(
                1L,
                null,
                null
        );
    }

    @Test
    void shouldReturnZeroCountsWhenNoEmployeesExist() {
        List<BigDecimal> salaries = List.of();

        when(analyticsRepository.findSalariesForDistribution(
                1L,
                null,
                null
        )).thenReturn(salaries);

        List<SalaryDistributionResponse> result = analyticsService.getSalaryDistribution(
                1L,
                null,
                null
        );

        assertNotNull(result);
        assertEquals(5, result.size());

        assertEquals("0-50000", result.get(0).range());
        assertEquals(0L, result.get(0).employeeCount());

        assertEquals("50001-100000", result.get(1).range());
        assertEquals(0L, result.get(1).employeeCount());

        assertEquals("100001-150000", result.get(2).range());
        assertEquals(0L, result.get(2).employeeCount());

        assertEquals("150001-200000", result.get(3).range());
        assertEquals(0L, result.get(3).employeeCount());

        assertEquals("200000+", result.get(4).range());
        assertEquals(0L, result.get(4).employeeCount());

        verify(analyticsRepository, times(1)).findSalariesForDistribution(
                1L,
                null,
                null
        );
    }

    @Test
    void shouldPassAllFiltersToRepository() {

        Long currencyId = 1L;
        Long countryId = 10L;
        String department = "Engineering";

        when(analyticsRepository.findSalariesForDistribution(
                currencyId,
                countryId,
                department
        )).thenReturn(List.of(
                new BigDecimal("85000")
        ));

        List<SalaryDistributionResponse> result =
                analyticsService.getSalaryDistribution(
                        currencyId,
                        countryId,
                        department
                );

        assertNotNull(result);
        assertEquals(5, result.size());

        verify(analyticsRepository, times(1)).findSalariesForDistribution(
                currencyId,
                countryId,
                department
        );
    }
}