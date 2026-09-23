package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.analytics.AnalyticsOverviewResponse;
import com.acme.salarymanagement.dto.analytics.CountryAnalyticsResponse;
import com.acme.salarymanagement.dto.analytics.DepartmentAnalyticsResponse;
import com.acme.salarymanagement.dto.analytics.SalaryDistributionResponse;
import com.acme.salarymanagement.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsOverviewResponse getOverview() {
        long totalEmployees = analyticsRepository.countEmployees();
        long totalCountries = analyticsRepository.countCountries();
        long totalDepartments = analyticsRepository.countDepartments();

        return new AnalyticsOverviewResponse(
                totalEmployees,
                totalCountries,
                totalDepartments
        );
    }

   public List<CountryAnalyticsResponse> getByCountry() {
        List<Object[]> rows = analyticsRepository.findCountryAnalytics();
        List<CountryAnalyticsResponse> response = new ArrayList<>();

        for (Object[] row : rows) {
                CountryAnalyticsResponse.CountryInfo country = new CountryAnalyticsResponse.CountryInfo(
                        (Long) row[0],
                        (String) row[1],
                        (String) row[2]
                );

                CountryAnalyticsResponse.CurrencyInfo currency = new CountryAnalyticsResponse.CurrencyInfo(
                        (Long) row[3],
                        (String) row[4],
                        (String) row[5],
                        (String) row[6]
                );

                response.add(
                        new CountryAnalyticsResponse(
                                country,
                                ((Number) row[7]).longValue(),
                                currency,
                                BigDecimal.valueOf(((Number) row[8]).doubleValue()).setScale(2, RoundingMode.HALF_UP),
                                (BigDecimal) row[9],
                                (BigDecimal) row[10]
                        )
                );
        }

        return response;
    }

    public List<DepartmentAnalyticsResponse> getByDepartment() {
        List<Object[]> rows = analyticsRepository.findDepartmentAnalytics();
        List<DepartmentAnalyticsResponse> response = new ArrayList<>();

        for (Object[] row : rows) {
                DepartmentAnalyticsResponse.CurrencyInfo currency = new DepartmentAnalyticsResponse.CurrencyInfo(
                        (Long) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4]
                );

                response.add(
                        new DepartmentAnalyticsResponse(
                                (String) row[0],
                                currency,
                                ((Number) row[5]).longValue(),
                                BigDecimal.valueOf(((Number) row[6]).doubleValue()).setScale(2, RoundingMode.HALF_UP)
                        )
                );
        }

        return response;
   }

    public List<SalaryDistributionResponse> getSalaryDistribution(
        Long currencyId,
        Long countryId,
        String department
    ) {

        List<BigDecimal> salaries = analyticsRepository.findSalariesForDistribution(
                currencyId,
                countryId,
                department
        );

        long range1 = 0;
        long range2 = 0;
        long range3 = 0;
        long range4 = 0;
        long range5 = 0;

        for (BigDecimal salary : salaries) {
            if (salary.compareTo(new BigDecimal("50000")) <= 0) {
                range1++;
            } else if (salary.compareTo(new BigDecimal("100000")) <= 0) {
                range2++;
            } else if (salary.compareTo(new BigDecimal("150000")) <= 0) {
                range3++;
            } else if (salary.compareTo(new BigDecimal("200000")) <= 0) {
                range4++;
            } else {
                range5++;
            }
        }

        return List.of(
                new SalaryDistributionResponse("0-50000", range1),
                new SalaryDistributionResponse("50001-100000", range2),
                new SalaryDistributionResponse("100001-150000", range3),
                new SalaryDistributionResponse("150001-200000", range4),
                new SalaryDistributionResponse("200000+", range5)
        );
    }
}