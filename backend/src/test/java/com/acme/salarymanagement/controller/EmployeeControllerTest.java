package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.dto.currency.CurrencyResponse;
import com.acme.salarymanagement.dto.employee.EmployeeDetailResponse;
import com.acme.salarymanagement.dto.employee.EmployeePageResponse;
import com.acme.salarymanagement.dto.employee.EmployeeResponse;
import com.acme.salarymanagement.service.EmployeeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

        verifyNoMoreInteractions(employeeService);
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

        verifyNoMoreInteractions(employeeService);
    }
}