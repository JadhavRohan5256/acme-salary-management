package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.dto.currency.CurrencyResponse;
import com.acme.salarymanagement.dto.employee.EmployeeDetailResponse;
import com.acme.salarymanagement.dto.employee.EmployeePageResponse;
import com.acme.salarymanagement.dto.employee.EmployeeResponse;
import com.acme.salarymanagement.entity.Employee;
import com.acme.salarymanagement.repository.EmployeeRepository;
import com.acme.salarymanagement.repository.EmployeeSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    private final EmployeeRepository employeeRepository;

    public EmployeePageResponse getEmployees(
            int page,
            int size,
            String search,
            Long countryId,
            String department,
            String sort
    ) {

        if (page < 0) {
            page = DEFAULT_PAGE;
        }

        if (size <= 0) {
            size = DEFAULT_SIZE;
        }

        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        Sort sorting = buildSort(sort);

        Pageable pageable = PageRequest.of(page, size, sorting);

        Specification<Employee> specification = Specification
        		.where(EmployeeSpecification.search(search))
        		.and(EmployeeSpecification.hasCountry(countryId))
        		.and(EmployeeSpecification.hasDepartment(department));

        Page<Employee> employeePage = employeeRepository.findAll(specification, pageable);

        List<EmployeeResponse> employees = employeePage.getContent()
        		.stream()
        		.map(this::toEmployeeResponse)
        		.toList();

        return new EmployeePageResponse(
                employees,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages()
        );
    }

    public EmployeeDetailResponse getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
        		.orElseThrow(() ->
		                new RuntimeException(
		                        "Employee not found: " + employeeId
		                )
		        );

        return toEmployeeDetailResponse(employee);
    }

    private Sort buildSort(String sort) {

        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "firstName");
        }

        String[] parts = sort.split(",");

        String field = parts[0];

        Sort.Direction direction = Sort.Direction.ASC;

        if (parts.length > 1) {
            direction = Sort.Direction.fromOptionalString(parts[1]).orElse(Sort.Direction.ASC);
        }

        String mappedField = switch (field) {
            case "firstName" -> "firstName";
            case "lastName" -> "lastName";
            case "department" -> "department";
            case "currentSalary" -> "currentSalary";
            case "country" -> "country.name";
            default -> "firstName";
        };

        return Sort.by(direction, mappedField);
    }

    private EmployeeResponse toEmployeeResponse(Employee employee) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),

                new CountryResponse(
                        employee.getCountry().getId(),
                        employee.getCountry().getName(),
                        employee.getCountry().getCode()
                ),

                employee.getDepartment(),
                employee.getDesignation(),
                employee.getCurrentSalary(),

                new CurrencyResponse(
                        employee.getCurrency().getId(),
                        employee.getCurrency().getCode(),
                        employee.getCurrency().getName(),
                        employee.getCurrency().getSymbol()
                )
        );
    }

    private EmployeeDetailResponse toEmployeeDetailResponse(Employee employee) {
        return new EmployeeDetailResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),

                new CountryResponse(
                        employee.getCountry().getId(),
                        employee.getCountry().getName(),
                        employee.getCountry().getCode()
                ),

                employee.getDepartment(),
                employee.getDesignation(),
                employee.getCurrentSalary(),

                new CurrencyResponse(
                        employee.getCurrency().getId(),
                        employee.getCurrency().getCode(),
                        employee.getCurrency().getName(),
                        employee.getCurrency().getSymbol()
                ),

                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}