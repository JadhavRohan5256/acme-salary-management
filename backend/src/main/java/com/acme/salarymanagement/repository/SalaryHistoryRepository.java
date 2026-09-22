package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.SalaryHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long> {

    List<SalaryHistory> findByEmployeeIdOrderByEffectiveDateDescIdDesc(
        Long employeeId
    );
}