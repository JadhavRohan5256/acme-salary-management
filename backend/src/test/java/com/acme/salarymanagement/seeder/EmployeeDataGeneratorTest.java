package com.acme.salarymanagement.seeder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeDataGeneratorTest {
    private EmployeeDataGenerator generator;

    @BeforeAll
    void beforeAll() {
    	SeedDataResourceReader resourceReader = new SeedDataResourceReader();
        this.generator = new EmployeeDataGenerator(resourceReader);
    }
    
    
    @Test
    void shouldGenerate10000Employees() {

        List<EmployeeDataGenerator.EmployeeSeedData> employees = generator.generate();

        assertEquals(10_000, employees.size());
    }

    @Test
    void shouldGenerateUniqueEmails() {

        List<EmployeeDataGenerator.EmployeeSeedData> employees = generator.generate();

        Set<String> emails = employees.stream()
                .map(EmployeeDataGenerator.EmployeeSeedData::email)
                .collect(Collectors.toSet());

        assertEquals(10_000, emails.size());
    }

    @Test
    void shouldGenerateEmployeesWithRequiredFields() {

        List<EmployeeDataGenerator.EmployeeSeedData> employees = generator.generate();

        assertFalse(employees.isEmpty());

        employees.forEach(employee -> {
            assertNotNull(employee.firstName());
            assertNotNull(employee.lastName());
            assertNotNull(employee.email());
            assertNotNull(employee.countryCode());
            assertNotNull(employee.department());
            assertNotNull(employee.designation());
            assertNotNull(employee.salary());
            assertNotNull(employee.currencyCode());
            assertTrue(employee.salary().signum() > 0);
        });
    }
}