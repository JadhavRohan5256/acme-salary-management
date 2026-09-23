import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { EmployeeQuery, EmployeeService } from './employee.service';
import { environment } from '../../../../environments/environment.development';
import { PageResponse } from '../../models/page';
import { Employee } from '../../models/employee';
import { SalaryHistory } from '../../models/salary-history';
import { SalaryUpdateRequest, SalaryUpdateResponse } from '../../models/salary-update';



describe('EmployeeService', () => {
  let service: EmployeeService;
  let httpMock: HttpTestingController;

  const apiUrl = `${environment.apiUrl}/employees`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ EmployeeService ]
    });

    service = TestBed.inject(EmployeeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get employees with default query', () => {
    const response: PageResponse<Employee> = {
      content: [],
      page: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0
    };

    service.getEmployees().subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(apiUrl);

    expect(request.request.method).toBe('GET');
    request.flush(response);
  });

  it('should send pagination parameters when getting employees', () => {
    const response: PageResponse<Employee> = {
      content: [],
      page: 1,
      size: 10,
      totalElements: 0,
      totalPages: 0
    };

    service.getEmployees({ page: 1, size: 10 }).subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(
      req =>
        req.url === apiUrl &&
        req.params.get('page') === '1' &&
        req.params.get('size') === '10'
    );

    expect(request.request.method).toBe('GET');
    request.flush(response);
  });

  it('should send search parameter when getting employees', () => {
    service.getEmployees({ search: 'john' }).subscribe();

    const request = httpMock.expectOne(
      req =>
        req.url === apiUrl &&
        req.params.get('search') === 'john'
    );

    expect(request.request.method).toBe('GET');

    request.flush({
      content: [],
      page: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0
    });
  });

  it('should send country filter parameter', () => {
    service.getEmployees({ countryId: 1 }).subscribe();

    const request = httpMock.expectOne(
      req =>
        req.url === apiUrl &&
        req.params.get('countryId') === '1'
    );

    expect(request.request.method).toBe('GET');
    request.flush({
      content: [],
      page: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0
    });
  });

  it('should send department filter parameter', () => {
    service.getEmployees({ department: 'Engineering' }).subscribe();

    const request = httpMock.expectOne(
      req =>
        req.url === apiUrl &&
        req.params.get('department') === 'Engineering'
    );

    expect(request.request.method).toBe('GET');
    request.flush({
      content: [],
      page: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0
    });
  });

  it('should send sort parameter', () => {
    service.getEmployees({ sort: 'lastName,asc' }).subscribe();

    const request = httpMock.expectOne(
      req =>
        req.url === apiUrl &&
        req.params.get('sort') === 'lastName,asc'
    );

    expect(request.request.method).toBe('GET');
    request.flush({
      content: [],
      page: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0
    });
  });

  it('should send all employee query parameters', () => {
    const query: EmployeeQuery = {
      page: 2,
      size: 50,
      search: 'john',
      countryId: 1,
      department: 'Engineering',
      sort: 'lastName,asc'
    };

    service.getEmployees(query).subscribe();

    const request = httpMock.expectOne(
      req =>
        req.url === apiUrl &&
        req.params.get('page') === '2' &&
        req.params.get('size') === '50' &&
        req.params.get('search') === 'john' &&
        req.params.get('countryId') === '1' &&
        req.params.get('department') ===
          'Engineering' &&
        req.params.get('sort') ===
          'lastName,asc'
    );

    expect(request.request.method).toBe('GET');

    request.flush({
      content: [],
      page: 2,
      size: 50,
      totalElements: 0,
      totalPages: 0
    });
  });


  it('should get employee by id', () => {
    const response: Employee = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@acme.com',
      country: {
        id: 1,
        name: 'India',
        code: 'IN'
      },
      department: 'Engineering',
      designation: 'Software Engineer',
      currentSalary: 75000,
      currency: {
        id: 1,
        code: 'INR',
        name: 'Indian Rupee',
        symbol: '₹'
      }
    };

    service.getEmployee(1).subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(`${apiUrl}/1`);

    expect(request.request.method).toBe('GET');
    request.flush(response);
  });


  it('should get salary history by employee id', () => {
    const response: SalaryHistory[] = [
      {
        id: 1,
        previousSalary: 65000,
        newSalary: 75000,
        currency: {
          id: 1,
          code: 'INR',
          name: 'Indian Rupee',
          symbol: '₹'
        },
        effectiveDate: '2026-09-01',
        changedBy: 'admin',
        createdAt: '2026-09-01T10:00:00'
      }
    ];

    service.getSalaryHistory(1).subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(`${apiUrl}/1/salary-history`);

    expect(request.request.method).toBe('GET');
    request.flush(response);
  });


  it('should update employee salary', () => {
    const requestBody: SalaryUpdateRequest = {
      newSalary: 80000,
      currencyId: 1,
      effectiveDate: '2026-09-23'
    };

    const response: SalaryUpdateResponse = {
      employeeId: 1,
      previousSalary: 75000,
      newSalary: 80000,
      currencyId: 1,
      effectiveDate: '2026-09-23',
      message: 'Salary updated successfully'
    };

    service.updateSalary(1, requestBody).subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(
      `${apiUrl}/1/salary`
    );

    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toEqual(requestBody);
    request.flush(response);
  });
});