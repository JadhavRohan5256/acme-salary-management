package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Employee;

import org.springframework.data.jpa.domain.Specification;

public final class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> search(String search) {
        return (root, query, criteriaBuilder) -> {

            if (search == null || search.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String value = "%" + search.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        value
                    ),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        value
                    ),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        value
                    )
            );
        };
    }

    public static Specification<Employee> hasCountry(Long countryId) {
        return (root, query, criteriaBuilder) -> {

            if (countryId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                root.get("country").get("id"),
                countryId
            );
        };
    }

    public static Specification<Employee> hasDepartment(String department) {
        return (root, query, criteriaBuilder) -> {

            if (department == null || department.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("department")),
                department.trim().toLowerCase()
            );
        };
    }
}