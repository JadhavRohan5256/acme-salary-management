package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.country.CountryResponse;
import com.acme.salarymanagement.dto.currency.CurrencyResponse;
import com.acme.salarymanagement.dto.employee.EmployeeDetailResponse;
import com.acme.salarymanagement.dto.employee.EmployeePageResponse;
import com.acme.salarymanagement.dto.employee.EmployeeResponse;
import com.acme.salarymanagement.dto.employee.SalaryHistoryResponse;
import com.acme.salarymanagement.dto.employee.SalaryUpdateRequest;
import com.acme.salarymanagement.dto.employee.SalaryUpdateResponse;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.entity.Employee;
import com.acme.salarymanagement.entity.SalaryHistory;
import com.acme.salarymanagement.entity.User;
import com.acme.salarymanagement.exception.BadRequestException;
import com.acme.salarymanagement.exception.ResourceNotFoundException;
import com.acme.salarymanagement.repository.CurrencyRepository;
import com.acme.salarymanagement.repository.EmployeeRepository;
import com.acme.salarymanagement.repository.EmployeeSpecification;
import com.acme.salarymanagement.repository.SalaryHistoryRepository;
import com.acme.salarymanagement.repository.UserRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    private final EmployeeRepository employeeRepository;
    private final CurrencyRepository currencyRepository;
    private final SalaryHistoryRepository salaryHistoryRepository;
    private final UserRepository userRepository;

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
		                new ResourceNotFoundException(
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
        
        String mappedField = switch (field) {
                case "firstName" -> "firstName";
                case "lastName" -> "lastName";
                case "department" -> "department";
                case "currentSalary" -> "currentSalary";
                case "country" -> "country.name";
                default -> throw new BadRequestException(
                        "Invalid sort field: " + field
                );
        };

        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length == 2) {
            direction = Sort.Direction.fromOptionalString(parts[1].trim())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Invalid sort direction: " + parts[1]
                        )
                );

        }


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
    
    public List<SalaryHistoryResponse> getSalaryHistory(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found: " + employeeId);
        }

        return salaryHistoryRepository
                .findByEmployeeIdOrderByEffectiveDateDescIdDesc(employeeId)
                .stream()
                .map(history -> new SalaryHistoryResponse(
                        history.getId(),
                        history.getPreviousSalary(),
                        history.getNewSalary(),
                        new CurrencyResponse(
                                history.getCurrency().getId(),
                                history.getCurrency().getCode(),
                                history.getCurrency().getName(),
                                history.getCurrency().getSymbol()
                        ),
                        history.getEffectiveDate(),
                        history.getChangedBy().getUsername(),
                        history.getCreatedAt()
                ))
                .toList();
    }
    
    @Transactional
    public SalaryUpdateResponse updateSalary(
            Long employeeId,
            SalaryUpdateRequest request
    ) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found: " + employeeId
                        )
                );

        Currency currency = currencyRepository.findById(request.currencyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Currency not found: " + request.currencyId()
                        )
                );

        User currentUser = getCurrentUser();

        BigDecimal previousSalary = employee.getCurrentSalary();

        employee.setCurrentSalary(request.newSalary());
        employee.setCurrency(currency);

        employeeRepository.save(employee);

        SalaryHistory salaryHistory = SalaryHistory.builder()
                .employee(employee)
                .previousSalary(previousSalary)
                .newSalary(request.newSalary())
                .currency(currency)
                .effectiveDate(request.effectiveDate())
                .changedBy(currentUser)
                .build();

        salaryHistoryRepository.save(salaryHistory);

        return new SalaryUpdateResponse(
                employee.getId(),
                previousSalary,
                employee.getCurrentSalary(),
                new CurrencyResponse(
                        currency.getId(),
                        currency.getCode(),
                        currency.getName(),
                        currency.getSymbol()
                ),
                request.effectiveDate(),
                currentUser.getUsername(),
                salaryHistory.getCreatedAt()
        );
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
        		.getContext()
        		.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not authenticated");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user not found: " + username
                        )
                );
    }
}