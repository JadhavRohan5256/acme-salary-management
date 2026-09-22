package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.employee.EmployeeDetailResponse;
import com.acme.salarymanagement.dto.employee.EmployeePageResponse;
import com.acme.salarymanagement.entity.Country;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.entity.Employee;
import com.acme.salarymanagement.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        Country country = Country.builder()
                .id(1L)
                .name("India")
                .code("IN")
                .build();

        Currency currency = Currency.builder()
                .id(1L)
                .code("INR")
                .name("Indian Rupee")
                .symbol("₹")
                .build();

        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@acme.example")
                .country(country)
                .department("Engineering")
                .designation("Software Engineer")
                .currentSalary(new BigDecimal("120000.00"))
                .currency(currency)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getEmployees_shouldReturnPaginatedEmployees() {
        Page<Employee> employeePage =new PageImpl<>(List.of(employee), PageRequest.of(0, 20), 1);

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);

        EmployeePageResponse response = employeeService.getEmployees(
                0,
                20,
                null,
                null,
                null,
                null
        );

        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(0, response.page());
        assertEquals(20, response.size());
        assertEquals(1, response.totalElements());
        assertEquals(1, response.totalPages());

        assertEquals("John", response.content().get(0).firstName());
        assertEquals("India", response.content().get(0).country().name());

        verify(employeeRepository).findAll(any(Specification.class),any(Pageable.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDetailResponse response = employeeService.getEmployeeById(1L);

        assertNotNull(response);

        assertEquals(1L, response.id());
        assertEquals("John", response.firstName());
        assertEquals("Smith", response.lastName());
        assertEquals("john.smith@acme.example", response.email());

        assertEquals("India", response.country().name());
        assertEquals("INR", response.currency().code());
        assertEquals(new BigDecimal("120000.00"),response.currentSalary());

        verify(employeeRepository).findById(1L);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void getEmployees_shouldLimitSizeTo100() {

        Page<Employee> employeePage = new PageImpl<>(
                List.of(employee),
                PageRequest.of(0, 100),
                1
        );

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);

        EmployeePageResponse response = employeeService.getEmployees(
                0,
                500,
                null,
                null,
                null,
                null
        );

        assertEquals(100, response.size());

        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getEmployees_shouldUseDefaultSizeWhenSizeIsInvalid() {
        Page<Employee> employeePage = new PageImpl<>(
                List.of(employee),
                PageRequest.of(0, 20),
                1
        );

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);

        EmployeePageResponse response = employeeService.getEmployees(
                0,
                0,
                null,
                null,
                null,
                null
        );

        assertEquals(20, response.size());

        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}