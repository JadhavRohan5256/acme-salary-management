import { HttpClientTestingModule, HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth/auth.service';
import { AuthInterceptor } from './auth.interceptor';

describe('AuthInterceptor', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authService = jasmine.createSpyObj(
      'AuthService',
      ['getToken', 'logout']
    );

    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [
        {
          provide: AuthService,
          useValue: authService
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthInterceptor,
          multi: true
        },
        provideHttpClientTesting()
      ]
    });

    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    const interceptor = TestBed.inject(HTTP_INTERCEPTORS);

    expect(interceptor).toBeTruthy();
  });

  it('should add Authorization header when token exists', () => {
    authService.getToken.and.returnValue('test-jwt-token');
    httpClient.get('/api/employees').subscribe();
    const request = httpMock.expectOne('/api/employees');

    expect(request.request.headers.get('Authorization')).toBe('Bearer test-jwt-token');
    request.flush({
      content: []
    });
  });

  it('should not add Authorization header when token does not exist', () => {
    authService.getToken.and.returnValue(null);
    httpClient.get('/api/employees').subscribe();
    const request = httpMock.expectOne('/api/employees');

    expect(request.request.headers.has('Authorization')).toBeFalse();
    request.flush({
      content: []
    });
  });

  it('should logout when status code is 401', () => {
    authService.getToken.and.returnValue('test-jwt-token');
    httpClient.get('/api/employees').subscribe({
      error: () => {}
    });

    const request = httpMock.expectOne('/api/employees');

    request.flush(
      {
        message: 'Unauthorized'
      },
      {
        status: 401,
        statusText: 'Unauthorized'
      }
    );
    expect(authService.logout).toHaveBeenCalled();
  });
});