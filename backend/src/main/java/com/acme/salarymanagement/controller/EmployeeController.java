package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.employee.EmployeeDetailResponse;
import com.acme.salarymanagement.dto.employee.EmployeePageResponse;
import com.acme.salarymanagement.dto.employee.SalaryHistoryResponse;
import com.acme.salarymanagement.dto.employee.SalaryUpdateRequest;
import com.acme.salarymanagement.dto.employee.SalaryUpdateResponse;
import com.acme.salarymanagement.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<EmployeePageResponse> getEmployees(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to zero")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than zero")
            @Max(value = 100, message = "Size cannot exceed 100")
            int size,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            Long countryId,

            @RequestParam(required = false)
            String department,

            @RequestParam(required = false)
            String sort
    ) {

        return ResponseEntity.ok(
                employeeService.getEmployees(
                        page,
                        size,
                        search,
                        countryId,
                        department,
                        sort
                )
        );
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDetailResponse> getEmployeeById(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }
    
    @GetMapping("/{employeeId}/salary-history")
    public ResponseEntity<List<SalaryHistoryResponse>> getSalaryHistory(
            @PathVariable Long employeeId
    ) {
        return ResponseEntity.ok(
                employeeService.getSalaryHistory(employeeId)
        );
    }

    @PutMapping("/{employeeId}/salary")
    public ResponseEntity<SalaryUpdateResponse> updateSalary(
            @PathVariable Long employeeId,
            @Valid @RequestBody SalaryUpdateRequest request
    ) {
        return ResponseEntity.ok(
                employeeService.updateSalary(employeeId, request)
        );
    }
}