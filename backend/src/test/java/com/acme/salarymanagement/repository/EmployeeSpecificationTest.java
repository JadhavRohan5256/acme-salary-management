package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Employee;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeSpecificationTest {
    @Mock
    private Root<Employee> root;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private Path<String> stringPath;
    @Mock
    private Predicate predicate;

    @Test
    void search_shouldReturnConjunctionWhenSearchIsNull() {
        when(criteriaBuilder.conjunction()).thenReturn(predicate);

        Specification<Employee> specification = EmployeeSpecification.search(null);

        Predicate result = specification.toPredicate(
            root,
            query,
            criteriaBuilder
        );

        assertNotNull(result);

        verify(criteriaBuilder).conjunction();
        verifyNoMoreInteractions(criteriaBuilder);
        verifyNoInteractions(root);
    }

    @Test
    void search_shouldReturnConjunctionWhenSearchIsBlank() {
        when(criteriaBuilder.conjunction()).thenReturn(predicate);

        Specification<Employee> specification = EmployeeSpecification.search("   ");

        Predicate result = specification.toPredicate(
            root,
            query,
            criteriaBuilder
        );

        assertNotNull(result);

        verify(criteriaBuilder).conjunction();
        verifyNoMoreInteractions(criteriaBuilder);
        verifyNoInteractions(root);
    }

    @Test
    void search_shouldCreatePredicateForFirstNameLastNameAndEmail() {
        when(root.<String>get("firstName")).thenReturn(stringPath);
        when(root.<String>get("lastName")).thenReturn(stringPath);
        when(root.<String>get("email")).thenReturn(stringPath);
        when(criteriaBuilder.lower(stringPath)).thenReturn(stringPath);
        when(criteriaBuilder.like(stringPath, "%john%")).thenReturn(predicate);
        when(criteriaBuilder.or(
            predicate,
            predicate,
            predicate
        )).thenReturn(predicate);

        Specification<Employee> specification = EmployeeSpecification.search("  John  ");

        Predicate result = specification.toPredicate(
            root,
            query,
            criteriaBuilder
        );

        assertNotNull(result);

        verify(root).<String>get("firstName");
        verify(root).<String>get("lastName");
        verify(root).<String>get("email");

        verify(criteriaBuilder, org.mockito.Mockito.times(3)).lower(stringPath);
        verify(criteriaBuilder, org.mockito.Mockito.times(3)).like(stringPath, "%john%");

        verify(criteriaBuilder).or(
            predicate,
            predicate,
            predicate
        );
    }

    @Test
    void hasCountry_shouldReturnConjunctionWhenCountryIdIsNull() {

        when(criteriaBuilder.conjunction()).thenReturn(predicate);

        Specification<Employee> specification = EmployeeSpecification.hasCountry(null);

        Predicate result = specification.toPredicate(
            root,
            query,
            criteriaBuilder
        );

        assertNotNull(result);

        verify(criteriaBuilder).conjunction();
        verifyNoMoreInteractions(criteriaBuilder);
        verifyNoInteractions(root);
    }

    @Test
    void hasCountry_shouldCreateEqualityPredicate() {
        @SuppressWarnings("unchecked")
        Path<Object> countryPath = org.mockito.Mockito.mock(Path.class);

        @SuppressWarnings("unchecked")
        Path<Long> countryIdPath = org.mockito.Mockito.mock(Path.class);

        when(root.get("country")).thenReturn(countryPath);
        when(countryPath.<Long>get("id")).thenReturn(countryIdPath);
        when(criteriaBuilder.equal(countryIdPath, 1L)).thenReturn(predicate);

        Specification<Employee> specification = EmployeeSpecification.hasCountry(1L);

        Predicate result = specification.toPredicate(
            root,
            query,
            criteriaBuilder
        );

        assertNotNull(result);

        verify(root).get("country");
        verify(countryPath).<Long>get("id");
        verify(criteriaBuilder).equal(countryIdPath, 1L);
    }

    @Test
    void hasDepartment_shouldReturnConjunctionWhenDepartmentIsNull() {
        when(criteriaBuilder.conjunction()).thenReturn(predicate);

        Specification<Employee> specification = EmployeeSpecification.hasDepartment(null);

        Predicate result = specification.toPredicate(
            root,
            query,
            criteriaBuilder
        );

        assertNotNull(result);

        verify(criteriaBuilder).conjunction();
        verifyNoInteractions(root);
    }

    @Test
    void hasDepartment_shouldReturnConjunctionWhenDepartmentIsBlank() {
        when(criteriaBuilder.conjunction()).thenReturn(predicate);

        Specification<Employee> specification = EmployeeSpecification.hasDepartment("   ");

        Predicate result = specification.toPredicate(
            root,
            query,
            criteriaBuilder
        );

        assertNotNull(result);

        verify(criteriaBuilder).conjunction();
        verifyNoMoreInteractions(criteriaBuilder);
        verifyNoInteractions(root);
    }

    @Test
    void hasDepartment_shouldCreateCaseInsensitiveEqualityPredicate() {
        when(root.<String>get("department")).thenReturn(stringPath);
        when(criteriaBuilder.lower(stringPath)).thenReturn(stringPath);
        when(criteriaBuilder.equal(
            stringPath,
            "engineering"
        )).thenReturn(predicate);

        Specification<Employee> specification =EmployeeSpecification.hasDepartment("  ENGINEERING  ");

        Predicate result = specification.toPredicate(
            root,
            query,
            criteriaBuilder
        );

        assertNotNull(result);

        verify(root).<String>get("department");
        verify(criteriaBuilder).lower(stringPath);
        verify(criteriaBuilder).equal(stringPath, "engineering");
    }

}







