import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { AuthService } from './auth.service';
import { LoginResponse } from '../../models/auth';
import { environment } from '../../../../environments/environment.development';


describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let router: jasmine.SpyObj<Router>;
  const apiUrl = environment.apiUrl;

  beforeEach(() => {
    router = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        AuthService,
        {
          provide: Router,
          useValue: router
        }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login and store token and user', () => {
    const loginRequest = {
      username: 'admin',
      password: 'password'
    };

    const response: LoginResponse = {
      accessToken: 'test-jwt-token',
      tokenType: 'Bearer',
      expiresIn: 3600,
      user: {
        id: 1,
        username: 'admin',
        role: 'HR_MANAGER'
      }
    };

    service.login(loginRequest).subscribe(result => {
      expect(result).toEqual(response);
      expect(localStorage.getItem('acme_access_token')).toBe('test-jwt-token');
      expect(JSON.parse(localStorage.getItem('acme_user')!)).toEqual(response.user);
    });

    const request = httpMock.expectOne(`${apiUrl}/auth/login`);

    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(loginRequest);
    request.flush(response);
  });

  it('should return token', () => {
    localStorage.setItem('acme_access_token', 'test-token');

    expect(service.getToken()).toBe('test-token');
  });

  it('should return user', () => {
    const user = {
      id: 1,
      username: 'admin',
      role: 'HR_MANAGER'
    };

    localStorage.setItem('acme_user', JSON.stringify(user));
    expect(service.getUser()).toEqual(user);
  });

  it('should return null when user is not stored', () => {
    expect(service.getUser()).toBeNull();
  });

  it('should return true when authenticated', () => {
    localStorage.setItem('acme_access_token', 'test-token');

    expect(service.isAuthenticated()).toBeTrue();
  });

  it('should return false when not authenticated', () => {
    expect(service.isAuthenticated()).toBeFalse();
  });

  it('should remove authentication data and navigate to login on logout', () => {
    localStorage.setItem('acme_access_token', 'test-token');

    localStorage.setItem(
      'acme_user',
      JSON.stringify({
        id: 1,
        username: 'admin',
        role: 'HR_MANAGER'
      })
    );

    service.logout();

    expect(service.getToken()).toBeNull();
    expect(service.getUser()).toBeNull();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});