package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.SalaryHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long> {

    List<SalaryHistory> findByEmployeeIdOrderByEffectiveDateDescIdDesc(
            Long employeeId
    );
}