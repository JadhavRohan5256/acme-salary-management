package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository 
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByCode(String code);
}