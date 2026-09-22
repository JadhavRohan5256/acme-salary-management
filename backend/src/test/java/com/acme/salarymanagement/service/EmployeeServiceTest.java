package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.employee.EmployeeDetailResponse;
import com.acme.salarymanagement.dto.employee.EmployeePageResponse;
import com.acme.salarymanagement.dto.employee.SalaryHistoryResponse;
import com.acme.salarymanagement.dto.employee.SalaryUpdateRequest;
import com.acme.salarymanagement.dto.employee.SalaryUpdateResponse;
import com.acme.salarymanagement.entity.Country;
import com.acme.salarymanagement.entity.Currency;
import com.acme.salarymanagement.entity.Employee;
import com.acme.salarymanagement.entity.SalaryHistory;
import com.acme.salarymanagement.entity.User;
import com.acme.salarymanagement.repository.CurrencyRepository;
import com.acme.salarymanagement.repository.EmployeeRepository;
import com.acme.salarymanagement.repository.SalaryHistoryRepository;
import com.acme.salarymanagement.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private SalaryHistoryRepository salaryHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Currency inrCurrency;
    private Currency usdCurrency;
    private User user;

    @BeforeEach
    void setUp() {
        Country country = Country.builder()
                .name("India")
                .code("IN")
                .build();

        inrCurrency = Currency.builder()

                .code("INR")

                .name("Indian Rupee")

                .symbol("₹")

                .build();

        usdCurrency = Currency.builder()

                .code("USD")

                .name("US Dollar")

                .symbol("$")

                .build();

        user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPasswordHash("password");
        user.setRole("ADMIN");

        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@acme.example")
                .country(country)
                .department("Engineering")
                .designation("Software Engineer")
                .currentSalary(new BigDecimal("120000.00"))
                .currency(inrCurrency)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getEmployees_shouldReturnPaginatedEmployees() {
        Page<Employee> employeePage =new PageImpl<>(List.of(employee), PageRequest.of(0, 20), 1);

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);

        EmployeePageResponse response = employeeService.getEmployees(
                0,
                20,
                null,
                null,
                null,
                null
        );

        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(0, response.page());
        assertEquals(20, response.size());
        assertEquals(1, response.totalElements());
        assertEquals(1, response.totalPages());

        assertEquals("John", response.content().get(0).firstName());
        assertEquals("India", response.content().get(0).country().name());

        verify(employeeRepository).findAll(any(Specification.class),any(Pageable.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDetailResponse response = employeeService.getEmployeeById(1L);

        assertNotNull(response);

        assertEquals(1L, response.id());
        assertEquals("John", response.firstName());
        assertEquals("Smith", response.lastName());
        assertEquals("john.smith@acme.example", response.email());

        assertEquals("India", response.country().name());
        assertEquals("INR", response.currency().code());
        assertEquals(new BigDecimal("120000.00"),response.currentSalary());

        verify(employeeRepository).findById(1L);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void getEmployees_shouldLimitSizeTo100() {

        Page<Employee> employeePage = new PageImpl<>(
                List.of(employee),
                PageRequest.of(0, 100),
                1
        );

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);

        EmployeePageResponse response = employeeService.getEmployees(
                0,
                500,
                null,
                null,
                null,
                null
        );

        assertEquals(100, response.size());

        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getEmployees_shouldUseDefaultSizeWhenSizeIsInvalid() {
        Page<Employee> employeePage = new PageImpl<>(
                List.of(employee),
                PageRequest.of(0, 20),
                1
        );

        when(employeeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(employeePage);

        EmployeePageResponse response = employeeService.getEmployees(
                0,
                0,
                null,
                null,
                null,
                null
        );

        assertEquals(20, response.size());

        verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void updateSalary_shouldUpdateEmployeeAndCreateSalaryHistory() {
        Employee employee = Employee.builder()
                .id(1L)
                .currentSalary(new BigDecimal("50000.00"))
                .build();

        Currency newCurrency = Currency.builder()
                .id(2L)
                .code("USD")
                .name("US Dollar")
                .symbol("$")
                .build();

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");

        SalaryUpdateRequest request = new SalaryUpdateRequest(
                new BigDecimal("60000.00"),
                2L,
                LocalDate.of(2026, 9, 22)
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(currencyRepository.findById(2L))
                .thenReturn(Optional.of(newCurrency));

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        when(salaryHistoryRepository.save(any(SalaryHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Authentication authentication =
        		new UsernamePasswordAuthenticationToken(
        		        "admin",
        		        null,
        		        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        		);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        SalaryUpdateResponse response =
                employeeService.updateSalary(1L, request);

        assertNotNull(response);

        assertEquals(1L, response.employeeId());

        assertEquals(
                new BigDecimal("50000.00"),
                response.previousSalary()
        );

        assertEquals(
                new BigDecimal("60000.00"),
                response.newSalary()
        );

        assertEquals(
                newCurrency.getId(),
                response.currency().id()
        );

        assertEquals(
                newCurrency.getCode(),
                response.currency().code()
        );

        assertEquals(
                newCurrency.getName(),
                response.currency().name()
        );

        assertEquals(
                newCurrency.getSymbol(),
                response.currency().symbol()
        );

        assertEquals(
                LocalDate.of(2026, 9, 22),
                response.effectiveDate()
        );

        assertEquals(
                "admin",
                response.updatedBy()
        );

        assertEquals(
                new BigDecimal("60000.00"),
                employee.getCurrentSalary()
        );

        assertEquals(
                newCurrency,
                employee.getCurrency()
        );

        ArgumentCaptor<SalaryHistory> salaryHistoryCaptor =
                ArgumentCaptor.forClass(SalaryHistory.class);

        verify(salaryHistoryRepository)
                .save(salaryHistoryCaptor.capture());

        SalaryHistory savedHistory =
                salaryHistoryCaptor.getValue();

        assertEquals(
                employee,
                savedHistory.getEmployee()
        );

        assertEquals(
                new BigDecimal("50000.00"),
                savedHistory.getPreviousSalary()
        );

        assertEquals(
                new BigDecimal("60000.00"),
                savedHistory.getNewSalary()
        );

        assertEquals(
                newCurrency,
                savedHistory.getCurrency()
        );

        assertEquals(
                LocalDate.of(2026, 9, 22),
                savedHistory.getEffectiveDate()
        );

        assertEquals(
                user,
                savedHistory.getChangedBy()
        );

        verify(employeeRepository).findById(1L);

        verify(currencyRepository).findById(2L);

        verify(userRepository).findByUsername("admin");

        verify(employeeRepository).save(employee);

        verify(salaryHistoryRepository).save(any(SalaryHistory.class));
    }

    @Test
    void updateSalary_shouldThrowException_whenEmployeeDoesNotExist() {
        SalaryUpdateRequest request = new SalaryUpdateRequest(
                new BigDecimal("125000.00"),
                2L,
                LocalDate.of(2026, 10, 1)
        );

        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> employeeService.updateSalary(999L, request)
        );

        assertEquals("Employee not found: 999", exception.getMessage());

        verify(employeeRepository).findById(999L);
        verify(currencyRepository, never()).findById(anyLong());
        verify(salaryHistoryRepository, never()).save(any());
    }

    @Test
    void updateSalary_shouldThrowException_whenCurrencyDoesNotExist() {
        SalaryUpdateRequest request = new SalaryUpdateRequest(
                new BigDecimal("125000.00"),
                999L,
                LocalDate.of(2026, 10, 1)
        );

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(currencyRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> employeeService.updateSalary(1L, request)
        );

        assertEquals("Currency not found: 999", exception.getMessage());

        verify(employeeRepository).findById(1L);
        verify(currencyRepository).findById(999L);
        verify(employeeRepository, never()).save(any());
        verify(salaryHistoryRepository, never()).save(any());
    }

    @Test
    void updateSalary_shouldThrowException_whenUserIsNotAuthenticated() {
        SalaryUpdateRequest request = new SalaryUpdateRequest(
                new BigDecimal("125000.00"),
                2L,
                LocalDate.of(2026, 10, 1)
        );

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(currencyRepository.findById(2L)).thenReturn(Optional.of(usdCurrency));

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> employeeService.updateSalary(1L, request)
        );

        assertEquals("User is not authenticated", exception.getMessage());

        verify(employeeRepository, never()).save(any());
        verify(salaryHistoryRepository, never()).save(any());
    }

    @Test
    void getSalaryHistory_shouldReturnSalaryHistory() {
        SalaryHistory history1 = SalaryHistory.builder()
                .id(2L)
                .employee(employee)
                .previousSalary(new BigDecimal("120000.00"))
                .newSalary(new BigDecimal("125000.00"))
                .currency(usdCurrency)
                .effectiveDate(LocalDate.of(2026, 10, 1))
                .changedBy(user)
                .build();

        history1.setCreatedAt(LocalDateTime.of(2026, 9, 22, 16, 0));

        SalaryHistory history2 = SalaryHistory.builder()
                .id(1L)
                .employee(employee)
                .previousSalary(new BigDecimal("100000.00"))
                .newSalary(new BigDecimal("120000.00"))
                .currency(inrCurrency)
                .effectiveDate(LocalDate.of(2026, 5, 1))
                .changedBy(user)
                .build();

        history2.setCreatedAt(LocalDateTime.of(2026, 4, 25, 10, 0));

        when(employeeRepository.existsById(1L)).thenReturn(true);

        when(salaryHistoryRepository.findByEmployeeIdOrderByEffectiveDateDescIdDesc(1L))
                .thenReturn(List.of(history1, history2));

        List<SalaryHistoryResponse> response = employeeService.getSalaryHistory(1L);

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(2L, response.get(0).id());
        assertEquals(new BigDecimal("120000.00"), response.get(0).previousSalary());
        assertEquals(new BigDecimal("125000.00"), response.get(0).newSalary());
        assertEquals("USD", response.get(0).currency().code());
        assertEquals(LocalDate.of(2026, 10, 1), response.get(0).effectiveDate());
        assertEquals("admin", response.get(0).changedBy());

        assertEquals(1L, response.get(1).id());
        assertEquals(new BigDecimal("100000.00"), response.get(1).previousSalary());
        assertEquals(new BigDecimal("120000.00"), response.get(1).newSalary());
        assertEquals("INR", response.get(1).currency().code());

        verify(employeeRepository).existsById(1L);
        verify(salaryHistoryRepository).findByEmployeeIdOrderByEffectiveDateDescIdDesc(1L);
    }

    @Test
    void getSalaryHistory_shouldReturnEmptyList_whenNoHistoryExists() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        when(salaryHistoryRepository.findByEmployeeIdOrderByEffectiveDateDescIdDesc(1L)).thenReturn(List.of());

        List<SalaryHistoryResponse> response = employeeService.getSalaryHistory(1L);

        assertNotNull(response);
        assertEquals(0, response.size());

        verify(employeeRepository).existsById(1L);
        verify(salaryHistoryRepository).findByEmployeeIdOrderByEffectiveDateDescIdDesc(1L);
    }

    @Test
    void getSalaryHistory_shouldThrowException_whenEmployeeDoesNotExist() {
        when(employeeRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> employeeService.getSalaryHistory(999L));

        assertEquals("Employee not found: 999", exception.getMessage());

        verify(employeeRepository).existsById(999L);
        verify(salaryHistoryRepository, never()).findByEmployeeIdOrderByEffectiveDateDescIdDesc(anyLong());
    }
    
    @AfterEach
    void afterEach() {
        SecurityContextHolder.clearContext();
    }
}