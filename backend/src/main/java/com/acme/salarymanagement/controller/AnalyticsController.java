package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.analytics.AnalyticsOverviewResponse;
import com.acme.salarymanagement.dto.analytics.CountryAnalyticsResponse;
import com.acme.salarymanagement.dto.analytics.DepartmentAnalyticsResponse;
import com.acme.salarymanagement.dto.analytics.SalaryDistributionResponse;
import com.acme.salarymanagement.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public AnalyticsOverviewResponse getOverview() {
        return analyticsService.getOverview();
    }

    @GetMapping("/by-country")
    public List<CountryAnalyticsResponse> getByCountry() {
        return analyticsService.getByCountry();
    }

    @GetMapping("/by-department")
    public List<DepartmentAnalyticsResponse> getByDepartment() {
        return analyticsService.getByDepartment();
    }

    @GetMapping("/salary-distribution")
    public List<SalaryDistributionResponse> getSalaryDistribution(
        @RequestParam Long currencyId,
        @RequestParam(required = false) Long countryId,
        @RequestParam(required = false) String department
    ) {
        return analyticsService.getSalaryDistribution(currencyId, countryId, department);
    }
}