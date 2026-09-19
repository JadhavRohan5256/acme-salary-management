package com.acme.salarymanagement.seeder;

import com.acme.salarymanagement.entity.Country;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.repository.CountryRepository;
import com.acme.salarymanagement.repository.CurrencyRepository;
import com.acme.salarymanagement.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EmployeeDataSeeder implements CommandLineRunner {
    private static final int BATCH_SIZE = 1_000;
    private final EmployeeDataGenerator employeeDataGenerator;
    private final EmployeeRepository employeeRepository;
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final JdbcTemplate jdbcTemplate;

    public EmployeeDataSeeder(
            EmployeeDataGenerator employeeDataGenerator,
            EmployeeRepository employeeRepository,
            CountryRepository countryRepository,
            CurrencyRepository currencyRepository,
            JdbcTemplate jdbcTemplate
    ) {
        this.employeeDataGenerator = employeeDataGenerator;
        this.employeeRepository = employeeRepository;
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // Prevent duplicate employee creation on application restart.
        if (employeeRepository.count() > 0) {
            System.out.println("Employee data already exists. Skipping employee seeding.");
            return;
        }

        System.out.println("Starting employee data seeding...");

        Map<String, Country> countries = countryRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Country::getCode, Function.identity()));

        Map<String, Currency> currencies = currencyRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Currency::getCode, Function.identity()));

        List<EmployeeDataGenerator.EmployeeSeedData> employees = employeeDataGenerator.generate();

        String sql = """
                INSERT INTO employees (
                    first_name,
                    last_name,
                    email,
                    country_id,
                    department,
                    designation,
                    current_salary,
                    currency_id
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        for (int start = 0; start < employees.size(); start += BATCH_SIZE) {
            int end = Math.min(start + BATCH_SIZE, employees.size());
            List<EmployeeDataGenerator.EmployeeSeedData> batch = employees.subList(start, end);

            jdbcTemplate.batchUpdate(
                    sql,
                    batch,
                    batch.size(),
                    (statement, employee) -> {

                        Country country = countries.get(employee.countryCode());
                        Currency currency = currencies.get(employee.currencyCode());

                        if (country == null) {
                            throw new IllegalStateException(
                                    "Country not found: " + employee.countryCode()
                            );
                        }

                        if (currency == null) {
                            throw new IllegalStateException(
                                    "Currency not found: " + employee.currencyCode()
                            );
                        }

                        statement.setString(1, employee.firstName());
                        statement.setString(2, employee.lastName());
                        statement.setString(3, employee.email());
                        statement.setLong(4, country.getId());
                        statement.setString(5, employee.department());
                        statement.setString(6, employee.designation());
                        statement.setBigDecimal(7, employee.salary());
                        statement.setLong(8, currency.getId());
                    }
            );

            System.out.printf("Inserted employees: %d/%d%n", end, employees.size());
        }

        System.out.println("Employee data seeding completed successfully.");
    }
}