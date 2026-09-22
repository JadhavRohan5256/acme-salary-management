package com.acme.salarymanagement.seeder;

import com.acme.salarymanagement.entity.Country;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.repository.CountryRepository;
import com.acme.salarymanagement.repository.CurrencyRepository;
import com.acme.salarymanagement.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeDataSeederTest {
    @Mock
    private EmployeeDataGenerator employeeDataGenerator;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;
    private EmployeeDataSeeder seeder;

    @BeforeEach
    void setUp() {
        seeder = new EmployeeDataSeeder(
                employeeDataGenerator,
                employeeRepository,
                countryRepository,
                currencyRepository,
                jdbcTemplate
        );
    }

    @Test
    void shouldSkipSeedingWhenEmployeesAlreadyExist() {
        when(employeeRepository.count()).thenReturn(5L);
        seeder.run();
        verify(employeeRepository).count();
        verify(employeeDataGenerator, never()).generate();
        verify(jdbcTemplate, never()).batchUpdate(
                anyString(),
                anyList(),
                anyInt(),
                any(ParameterizedPreparedStatementSetter.class)
        );
    }

    @Test
    void shouldSeedEmployeesWhenDatabaseIsEmpty() {
    	Country country = mock(Country.class);
    	Currency currency = mock(Currency.class);

    	when(employeeRepository.count()).thenReturn(0L);
        when(country.getCode()).thenReturn("IN");
        when(currency.getCode()).thenReturn("INR");
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(currencyRepository.findAll()).thenReturn(List.of(currency));

        List<EmployeeDataGenerator.EmployeeSeedData> employees =
                List.of(
                        new EmployeeDataGenerator.EmployeeSeedData(
                                "John",
                                "Smith",
                                "john.smith1@acme.com",
                                "IN",
                                "IT",
                                "Developer",
                                new BigDecimal("75000.00"),
                                "INR"
                        )
                );

        when(employeeDataGenerator.generate()).thenReturn(employees);

        seeder.run();

        verify(employeeRepository).count();
        verify(countryRepository).findAll();
        verify(currencyRepository).findAll();
        verify(employeeDataGenerator).generate();
        verify(jdbcTemplate).batchUpdate(
                contains("INSERT INTO employees"),
                eq(employees),
                eq(1),
                any(ParameterizedPreparedStatementSetter.class)
        );
    }

    @Test
    void shouldInsertEmployeesInBatchesOf1000() {
    	Country country = mock(Country.class);
    	Currency currency = mock(Currency.class);

    	when(employeeRepository.count()).thenReturn(0L);
        when(country.getCode()).thenReturn("IN");
        when(currency.getCode()).thenReturn("INR");
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(currencyRepository.findAll()).thenReturn(List.of(currency));

        List<EmployeeDataGenerator.EmployeeSeedData> employees = IntStream.rangeClosed(1, 2500)
                        .mapToObj(i ->
                                new EmployeeDataGenerator.EmployeeSeedData(
                                        "John",
                                        "Smith",
                                        "john.smith" + i + "@acme.com",
                                        "IN",
                                        "IT",
                                        "Developer",
                                        new BigDecimal("75000.00"),
                                        "INR"
                                )
                        )
                        .toList();

        when(employeeDataGenerator.generate()).thenReturn(employees);
        seeder.run();
        verify(jdbcTemplate, times(3)).batchUpdate(
        		contains("INSERT INTO employees"),
        		anyList(),
        		anyInt(),
        		any(ParameterizedPreparedStatementSetter.class)
        );
    }

    @Test
    void shouldContinueSeedingWhenCountryDoesNotExist() {

        when(employeeRepository.count()).thenReturn(0L);
        when(countryRepository.findAll()).thenReturn(List.of());
        when(currencyRepository.findAll()).thenReturn(List.of());

        List<EmployeeDataGenerator.EmployeeSeedData> employees =
                List.of(
                        new EmployeeDataGenerator.EmployeeSeedData(
                                "John",
                                "Smith",
                                "john.smith1@acme.com",
                                "IN",
                                "IT",
                                "Developer",
                                new BigDecimal("75000.00"),
                                "INR"
                        )
                );

        when(employeeDataGenerator.generate()).thenReturn(employees);

        assertDoesNotThrow(() -> seeder.run());

        verify(employeeDataGenerator).generate();

        verify(jdbcTemplate).batchUpdate(
                contains("INSERT INTO employees"),
                eq(employees),
                eq(1),
                any(ParameterizedPreparedStatementSetter.class)
        );
    }

    @Test
    void shouldContinueSeedingWhenCurrencyDoesNotExist() {

        Country country = mock(Country.class);

        when(employeeRepository.count()).thenReturn(0L);
        when(country.getCode()).thenReturn("IN");
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(currencyRepository.findAll()).thenReturn(List.of());

        List<EmployeeDataGenerator.EmployeeSeedData> employees =
                List.of(
                        new EmployeeDataGenerator.EmployeeSeedData(
                                "John",
                                "Smith",
                                "john.smith1@acme.com",
                                "IN",
                                "IT",
                                "Developer",
                                new BigDecimal("75000.00"),
                                "INR"
                        )
                );

        when(employeeDataGenerator.generate()).thenReturn(employees);

        assertDoesNotThrow(() -> seeder.run());

        verify(employeeDataGenerator).generate();

        verify(jdbcTemplate).batchUpdate(
                contains("INSERT INTO employees"),
                eq(employees),
                eq(1),
                any(ParameterizedPreparedStatementSetter.class)
        );
    }
}