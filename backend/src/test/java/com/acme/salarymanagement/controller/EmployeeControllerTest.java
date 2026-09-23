package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.dto.currency.CurrencyResponse;
import com.acme.salarymanagement.dto.employee.EmployeeDetailResponse;
import com.acme.salarymanagement.dto.employee.EmployeePageResponse;
import com.acme.salarymanagement.dto.employee.EmployeeResponse;
import com.acme.salarymanagement.service.EmployeeService;
import com.acme.salarymanagement.dto.employee.SalaryHistoryResponse;
import com.acme.salarymanagement.dto.employee.SalaryUpdateRequest;
import com.acme.salarymanagement.dto.employee.SalaryUpdateResponse;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getEmployees_shouldReturnSuccessfulResponse() {
        EmployeeResponse employee = new EmployeeResponse(
                1L,
                "John",
                "Smith",
                "john.smith@acme.example",
                new CountryResponse(
                        2L,
                        "United States",
                        "US"
                ),
                "Engineering",
                "Senior Software Engineer",
                new BigDecimal("120000.00"),
                new CurrencyResponse(
                        2L,
                        "USD",
                        "US Dollar",
                        "$"
                )
        );

        EmployeePageResponse expectedResponse =
                new EmployeePageResponse(
                        List.of(employee),
                        0,
                        20,
                        1,
                        1
                );

        when(employeeService.getEmployees(
                0,
                20,
                null,
                null,
                null,
                null
        )).thenReturn(expectedResponse);

        ResponseEntity<EmployeePageResponse> actualResponse =
                employeeController.getEmployees(
                        0,
                        20,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(
                HttpStatus.OK,
                actualResponse.getStatusCode()
        );

        assertSame(
                expectedResponse,
                actualResponse.getBody()
        );

        verify(employeeService).getEmployees(
                0,
                20,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void getEmployeeById_shouldReturnSuccessfulResponse() {
        EmployeeDetailResponse expectedResponse =
                new EmployeeDetailResponse(
                        1L,
                        "John",
                        "Smith",
                        "john.smith@acme.example",
                        new CountryResponse(
                                2L,
                                "United States",
                                "US"
                        ),
                        "Engineering",
                        "Senior Software Engineer",
                        new BigDecimal("120000.00"),
                        new CurrencyResponse(
                                2L,
                                "USD",
                                "US Dollar",
                                "$"
                        ),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );

        when(employeeService.getEmployeeById(1L))
                .thenReturn(expectedResponse);

        ResponseEntity<EmployeeDetailResponse> actualResponse =
                employeeController.getEmployeeById(1L);

        assertEquals(
                HttpStatus.OK,
                actualResponse.getStatusCode()
        );

        assertSame(
                expectedResponse,
                actualResponse.getBody()
        );

        verify(employeeService).getEmployeeById(1L);
    }
    
    
    @Test
    void getSalaryHistory_shouldReturnSuccessfulResponse() {
        SalaryHistoryResponse history = new SalaryHistoryResponse(
                1L,
                new BigDecimal("100000.00"),
                new BigDecimal("120000.00"),
                new CurrencyResponse(
                        2L,
                        "USD",
                        "US Dollar",
                        "$"
                ),
                LocalDate.of(2026, 9, 1),
                "admin",
                LocalDateTime.now()
        );

        List<SalaryHistoryResponse> expectedResponse =
                List.of(history);

        when(employeeService.getSalaryHistory(1L))
                .thenReturn(expectedResponse);

        ResponseEntity<List<SalaryHistoryResponse>> actualResponse =
                employeeController.getSalaryHistory(1L);

        assertEquals(
                HttpStatus.OK,
                actualResponse.getStatusCode()
        );

        assertSame(
                expectedResponse,
                actualResponse.getBody()
        );

        verify(employeeService).getSalaryHistory(1L);
    }

    @Test
    void updateSalary_shouldReturnSuccessfulResponse() {
        SalaryUpdateRequest request = new SalaryUpdateRequest(
                new BigDecimal("120000.00"),
                2L,
                LocalDate.of(2026, 9, 1)
        );

        SalaryUpdateResponse expectedResponse =new SalaryUpdateResponse(
                1L,
                new BigDecimal("100000.00"),
                new BigDecimal("120000.00"),
                new CurrencyResponse(
                        2L,
                        "USD",
                        "US Dollar",
                        "$"
                ),
                LocalDate.of(2026, 9, 1),
                "admin",
                LocalDateTime.now()
        );

        when(employeeService.updateSalary(1L, request)).thenReturn(expectedResponse);

        ResponseEntity<SalaryUpdateResponse> actualResponse = employeeController.updateSalary(1L, request);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertSame(expectedResponse, actualResponse.getBody());

        verify(employeeService).updateSalary(1L, request);
    }
}