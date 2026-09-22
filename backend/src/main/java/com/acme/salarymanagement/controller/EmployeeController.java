package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.employee.EmployeeDetailResponse;
import com.acme.salarymanagement.dto.employee.EmployeePageResponse;
import com.acme.salarymanagement.service.EmployeeService;

import lombok.RequiredArgsConstructor;

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
            int page,

            @RequestParam(defaultValue = "10")
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
}