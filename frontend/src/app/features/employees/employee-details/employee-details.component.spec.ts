import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';

import { EmployeeDetailsComponent } from './employee-details.component';
import { loadEmployee } from '../../../store/employees/employee.actions';
import { Employee } from '../../../core/models/employee';


describe('EmployeeDetailsComponent', () => {
  let component: EmployeeDetailsComponent;
  let fixture: ComponentFixture<EmployeeDetailsComponent>;
  let store: jasmine.SpyObj<Store>;
  let router: jasmine.SpyObj<Router>;

  const employee: Employee = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
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

  beforeEach(async () => {
    store = jasmine.createSpyObj<Store>(
      'Store',
      [
        'select',
        'dispatch'
      ]
    );

    store.select.and.returnValue(of(employee));

    router = jasmine.createSpyObj<Router>(
      'Router',
      [
        'navigate'
      ]
    );

    const activatedRoute = {
      snapshot: {
        paramMap: {
          get: jasmine.createSpy('get').and.returnValue('1')
        }
      }
    };

    await TestBed.configureTestingModule({
      declarations: [
        EmployeeDetailsComponent
      ],
      providers: [
        {
          provide: Store,
          useValue: store
        },
        {
          provide: Router,
          useValue: router
        },
        {
          provide: ActivatedRoute,
          useValue: activatedRoute
        }
      ],
      schemas: [
        NO_ERRORS_SCHEMA
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(
      EmployeeDetailsComponent
    );

    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should read employee id from route', () => {
    expect(component.employeeId).toBe(1);
  });

  it('should dispatch loadEmployee on initialization', () => {
    expect(store.dispatch).toHaveBeenCalledWith(
      loadEmployee({
        employeeId: 1
      })
    );
  });

  it('should expose employee observable', (done) => {
    component.employee$.subscribe(result => {
      expect(result).toEqual(employee);
      done();
    });
  });

  it('should navigate back to employees', () => {
    component.goBack();

    expect(router.navigate).toHaveBeenCalledWith([
      '/employees'
    ]);
  });

  it('should navigate back when employee id is invalid', () => {
    store.dispatch.calls.reset();
    router.navigate.calls.reset();

    const activatedRoute = TestBed.inject(ActivatedRoute);

    (activatedRoute.snapshot.paramMap.get as jasmine.Spy).and.returnValue('0');
    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(['/employees']);
    expect(store.dispatch).not.toHaveBeenCalled();
  });
});
