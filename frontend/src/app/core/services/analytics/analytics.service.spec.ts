import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AnalyticsService } from './analytics.service';
import { environment } from '../../../../environments/environment.development';


describe('AnalyticsService', () => {
  let service: AnalyticsService;
  let httpMock: HttpTestingController;
  const apiUrl = `${environment.apiUrl}/analytics`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        AnalyticsService
      ]
    });

    service = TestBed.inject(
      AnalyticsService
    );

    httpMock = TestBed.inject(
      HttpTestingController
    );
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get analytics overview', () => {
    const response = {
      totalEmployees: 10000,
      totalCountries: 8,
      totalDepartments: 7
    };

    service.getOverview().subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(`${apiUrl}/overview`);

    expect(request.request.method).toBe('GET');
    request.flush(response);
  });

  it('should get country analytics', () => {
    const response = [
      {
        country: {
          id: 1,
          name: 'India',
          code: 'IN'
        },
        employeeCount: 3200,
        currency: {
          id: 1,
          code: 'INR',
          name: 'Indian Rupee',
          symbol: '₹'
        },
        averageSalary: 2100000,
        minimumSalary: 500000,
        maximumSalary: 6000000
      }
    ];

    service.getByCountry().subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(`${apiUrl}/by-country`);

    expect(request.request.method).toBe('GET');
    request.flush(response);
  });

  it('should get department analytics', () => {
    const response = [
      {
        department: 'Engineering',
        currency: {
          id: 1,
          code: 'INR',
          name: 'Indian Rupee',
          symbol: '₹'
        },
        employeeCount: 1250,
        averageSalary: 2100000
      }
    ];

    service.getByDepartment().subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(`${apiUrl}/by-department`);

    expect(request.request.method).toBe('GET');
    request.flush(response);
  });

  it('should get salary distribution with currency', () => {
    const response = [
      {
        range: '0-50000',
        employeeCount: 850
      },
      {
        range: '50001-100000',
        employeeCount: 3200
      }
    ];

    service.getSalaryDistribution({ currencyId: 2 }).subscribe(result => {
      expect(result).toEqual(response);
    });

    const request = httpMock.expectOne(
      request =>
        request.url === `${apiUrl}/salary-distribution` &&
        request.params.get('currencyId') === '2'
    );

    expect(request.request.method).toBe('GET');
    expect(request.request.params.has('countryId')).toBeFalse();
    expect(request.request.params.has('department')).toBeFalse();
    request.flush(response);
  });

  it('should get salary distribution with country filter', () => {
    service.getSalaryDistribution({ currencyId: 2, countryId: 5 }).subscribe();

    const request = httpMock.expectOne(
      request =>
        request.url ===
        `${apiUrl}/salary-distribution` &&
        request.params.get('currencyId') === '2' &&
        request.params.get('countryId') === '5'
    );

    expect(request.request.method).toBe('GET');
    request.flush([]);
  });

  it('should get salary distribution with department filter', () => {
    service.getSalaryDistribution({ currencyId: 2, department: 'Engineering' }).subscribe();

    const request = httpMock.expectOne(
      request =>
        request.url === `${apiUrl}/salary-distribution` &&
        request.params.get('currencyId') === '2' &&
        request.params.get('department') === 'Engineering'
    );

    expect(request.request.method).toBe('GET');
    request.flush([]);
  });

  it('should get salary distribution with all filters', () => {
    service.getSalaryDistribution({
      currencyId: 2,
      countryId: 5,
      department: 'Engineering'
    }).subscribe();

    const request = httpMock.expectOne(
      request =>
        request.url === `${apiUrl}/salary-distribution` &&
        request.params.get('currencyId') === '2' &&
        request.params.get('countryId') === '5' &&
        request.params.get('department') === 'Engineering'
    );

    expect(request.request.method).toBe('GET');
    request.flush([]);
  });

  it('should handle overview API error', () => {
    let errorResponse: unknown;

    service.getOverview().subscribe({
      next: () => fail('Expected an error'),
      error: error => {
        errorResponse = error;
      }
    });

    const request = httpMock.expectOne(`${apiUrl}/overview`);

    request.flush(
      {
        message: 'Failed to load analytics'
      },
      {
        status: 500,
        statusText: 'Server Error'
      }
    );

    expect(errorResponse).toBeTruthy();
  });

  it('should handle country analytics API error', () => {
    let errorResponse: unknown;

    service.getByCountry().subscribe({
      next: () => fail('Expected an error'),
      error: error => {
        errorResponse = error;
      }
    });

    const request = httpMock.expectOne(
      `${apiUrl}/by-country`
    );

    request.flush(
      {
        message: 'Failed to load country analytics'
      },
      {
        status: 500,
        statusText: 'Server Error'
      }
    );

    expect(errorResponse).toBeTruthy();
  });

  it('should handle department analytics API error', () => {
    let errorResponse: unknown;

    service.getByDepartment().subscribe({
      next: () => fail('Expected an error'),
      error: error => {
        errorResponse = error;
      }
    });

    const request = httpMock.expectOne(`${apiUrl}/by-department`);

    request.flush(
      {
        message: 'Failed to load department analytics'
      },
      {
        status: 500,
        statusText: 'Server Error'
      }
    );

    expect(errorResponse).toBeTruthy();
  });

  it('should handle salary distribution API error', () => {
    let errorResponse: unknown;

    service.getSalaryDistribution({ currencyId: 2 }).subscribe({
      next: () => fail('Expected an error'),
      error: error => {
        errorResponse = error;
      }
    });

    const request = httpMock.expectOne(
      request => request.url === `${apiUrl}/salary-distribution`
    );

    request.flush(
      {
        message: 'Failed to load salary distribution'
      },
      {
        status: 500,
        statusText: 'Server Error'
      }
    );
    expect(errorResponse).toBeTruthy();
  });
});
