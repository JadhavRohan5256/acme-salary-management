package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository 
public interface AnalyticsRepository extends JpaRepository<Employee, Long> {

    /*
     * Total employees
     */
    @Query("""
            SELECT COUNT(e)
            FROM Employee e
        """)
    long countEmployees();

    /*
     * Total countries
     */
    @Query("""
            SELECT COUNT(DISTINCT e.country.id)
            FROM Employee e
        """)
    long countCountries();

    /*
     * Total departments
     */
    @Query("""
            SELECT COUNT(DISTINCT e.department)
            FROM Employee e
        """)
    long countDepartments();

    /*
     * Country analytics
     *
     * Returns:
     * country id
     * country name
     * country code
     * employee count
     * currency id
     * currency code
     * currency name
     * currency symbol
     * average salary
     * minimum salary
     * maximum salary
     *
     * Grouping includes currency because salaries must not
     * be aggregated across different currencies.
     */
    @Query("""
            SELECT
                e.country.id,
                e.country.name,
                e.country.code,
                e.currency.id,
                e.currency.code,
                e.currency.name,
                e.currency.symbol,
                COUNT(e),
                AVG(e.currentSalary),
                MIN(e.currentSalary),
                MAX(e.currentSalary)
            FROM Employee e
            GROUP BY
                e.country.id,
                e.country.name,
                e.country.code,
                e.currency.id,
                e.currency.code,
                e.currency.name,
                e.currency.symbol
            ORDER BY e.country.name, e.currency.code
        """)
    List<Object[]> findCountryAnalytics();

    /*
     * Department analytics
     *
     * Grouping by currency prevents calculating an average
     * salary by combining INR, USD, GBP, etc.
     */
    @Query("""
            SELECT
                e.department,
                e.currency.id,
                e.currency.code,
                e.currency.name,
                e.currency.symbol,
                COUNT(e),
                AVG(e.currentSalary)
            FROM Employee e
            GROUP BY
                e.department,
                e.currency.id,
                e.currency.code,
                e.currency.name,
                e.currency.symbol
            ORDER BY e.department, e.currency.code
        """)
    List<Object[]> findDepartmentAnalytics();

    /*
     * Salary distribution
     *
     * currencyId is mandatory.
     * countryId and department are optional.
     */
    @Query("""
            SELECT e.currentSalary
            FROM Employee e
            WHERE e.currency.id = :currencyId
              AND (:countryId IS NULL OR e.country.id = :countryId)
              AND (:department IS NULL OR e.department = :department)
        """)
    List<BigDecimal> findSalariesForDistribution(
        @Param("currencyId") Long currencyId,
        @Param("countryId") Long countryId,
        @Param("department") String department
    );
}