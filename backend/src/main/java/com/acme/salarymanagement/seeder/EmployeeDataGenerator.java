package com.acme.salarymanagement.seeder;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class EmployeeDataGenerator {
    private static final int EMPLOYEE_COUNT = 10_000;
    private static final long RANDOM_SEED = 42L;

    private final List<String> firstNames;
    private final List<String> lastNames;
    private final List<String> departments;
    private final List<String> designations;
    private final List<String> countryCodes;
    private final List<String> currencyCodes;

    public EmployeeDataGenerator(SeedDataResourceReader resourceReader) {
        this.firstNames = resourceReader.read("first-names.txt");
        this.lastNames = resourceReader.read("last-names.txt");
        this.departments = resourceReader.read("departments.txt");
        this.designations = resourceReader.read("designations.txt");
        this.countryCodes = resourceReader.read("countries.txt");
        this.currencyCodes = resourceReader.read("currencies.txt");
    }

    public List<EmployeeSeedData> generate() {
        Random random = new Random(RANDOM_SEED);
        List<EmployeeSeedData> employees = new ArrayList<>(EMPLOYEE_COUNT);

        for (int i = 1; i <= EMPLOYEE_COUNT; i++) {
            String firstName = randomElement(firstNames, random);
            String lastName = randomElement(lastNames, random);
            String department = randomElement(departments, random);
            String designation = randomElement(designations, random);
            String countryCode = randomElement(countryCodes, random);
            String currencyCode = randomElement(currencyCodes, random);
            BigDecimal salary = generateSalary(random);
            String email = generateEmail(firstName, lastName, i);
            EmployeeSeedData employeeSeedData = new EmployeeSeedData(firstName, lastName, email, countryCode, department, designation, salary, currencyCode);
            
            employees.add(employeeSeedData);
        }

        return employees;
    }

    private String randomElement(List<String> values, Random random) {
        return values.get(random.nextInt(values.size()));
    }

    private BigDecimal generateSalary(Random random) {
        int salary = 40_000 + random.nextInt(160_001);

        return BigDecimal.valueOf(salary).setScale(2);
    }

    private String generateEmail(String firstName, String lastName, int employeeNumber) {
        return String.format(
                "%s.%s%d@acme.com",
                firstName.toLowerCase(),
                lastName.toLowerCase(),
                employeeNumber
        );
    }

    public record EmployeeSeedData(
            String firstName,
            String lastName,
            String email,
            String countryCode,
            String department,
            String designation,
            BigDecimal salary,
            String currencyCode
    ) {}
}