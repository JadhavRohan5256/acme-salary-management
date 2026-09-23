import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ReferenceDataService } from './reference-data.service';
import { environment } from '../../../../environments/environment.development';
import { Country } from '../../models/country';
import { Currency } from '../../models/currency';

describe('ReferenceDataService', () => {
  let service: ReferenceDataService;
  let httpMock: HttpTestingController;

  const apiUrl = environment.apiUrl;

  const countries: Country[] = [
    {
      id: 1,
      name: 'India',
      code: 'IN'
    },
    {
      id: 2,
      name: 'United States',
      code: 'US'
    }
  ];

  const currencies: Currency[] = [
    {
      id: 1,
      code: 'INR',
      name: 'Indian Rupee',
      symbol: '₹'
    },
    {
      id: 2,
      code: 'USD',
      name: 'US Dollar',
      symbol: '$'
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(ReferenceDataService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getCountries', () => {
    it('should fetch countries', () => {
      service.getCountries().subscribe(response => {
        expect(response).toEqual(countries);
        expect(response.length).toBe(2);
      });

      const request = httpMock.expectOne(`${apiUrl}/countries`);

      expect(request.request.method).toBe('GET');
      request.flush(countries);
    });

    it('should return an empty array when no countries exist', () => {
      service.getCountries().subscribe(response => {
        expect(response).toEqual([]);
      });

      const request = httpMock.expectOne(`${apiUrl}/countries`);

      expect(request.request.method).toBe('GET');
      request.flush([]);
    });

    it('should propagate country API errors', () => {
      service.getCountries().subscribe({
        next: () => fail('Expected an error'),
        error: error => {
          expect(error.status).toBe(500);
        }
      });

      const request = httpMock.expectOne(`${apiUrl}/countries`);

      request.flush(
        {
          message: 'Failed to load countries'
        },
        {
          status: 500,
          statusText: 'Internal Server Error'
        }
      );
    });
  });

  describe('getCurrencies', () => {
    it('should fetch currencies', () => {
      service.getCurrencies().subscribe(response => {
        expect(response).toEqual(currencies);
        expect(response.length).toBe(2);
      });

      const request = httpMock.expectOne(`${apiUrl}/currencies`);

      expect(request.request.method).toBe('GET');
      request.flush(currencies);
    });

    it('should return an empty array when no currencies exist', () => {
      service.getCurrencies().subscribe(response => {
        expect(response).toEqual([]);
      });

      const request = httpMock.expectOne(`${apiUrl}/currencies`);

      expect(request.request.method).toBe('GET');
      request.flush([]);
    });

    it('should propagate currency API errors', () => {
      service.getCurrencies().subscribe({
        next: () => fail('Expected an error'),
        error: error => {
          expect(error.status).toBe(500);
        }
      });

      const request = httpMock.expectOne(`${apiUrl}/currencies`);

      request.flush(
        {
          message: 'Failed to load currencies'
        },
        {
          status: 500,
          statusText: 'Internal Server Error'
        }
      );
    });
  });
});
