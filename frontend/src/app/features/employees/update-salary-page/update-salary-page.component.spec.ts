import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';

import { UpdateSalaryPageComponent } from './update-salary-page.component';
import { loadEmployee } from '../../../store/employees/employee.actions';
import { selectSelectedEmployee, selectSelectedEmployeeLoading, selectSelectedEmployeeError } from '../../../store/employees/employee.selectors';
import { Employee } from '../../../core/models/employee';

describe('UpdateSalaryPageComponent', () => {
  let component: UpdateSalaryPageComponent;
  let fixture: ComponentFixture<UpdateSalaryPageComponent>;
  let store: jasmine.SpyObj<Store>;
  let router: jasmine.SpyObj<Router>;

  const employee: Employee = {
    id: 101,
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
      code: 'USD',
      name: 'US Dollar',
      symbol: '$'
    }
  };

  beforeEach(async () => {
    store = jasmine.createSpyObj<Store>('Store', ['select', 'dispatch']);
    router = jasmine.createSpyObj<Router>('Router', ['navigate']);

    store.select.and.callFake((selector: unknown) => {
      if (selector === selectSelectedEmployee) {
        return of(employee);
      }

      if (selector === selectSelectedEmployeeLoading) {
        return of(false);
      }

      if (selector === selectSelectedEmployeeError) {
        return of(null);
      }

      return of(null);
    });

    await TestBed.configureTestingModule({
      declarations: [UpdateSalaryPageComponent],
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
          useValue: {
            snapshot: {
              paramMap: {
                get: jasmine.createSpy('get').and.returnValue('101')
              }
            }
          }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(UpdateSalaryPageComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize employee id from route', () => {
    component.ngOnInit();

    expect(component.employeeId).toBe(101);
  });

  it('should dispatch loadEmployee with route employee id', () => {
    component.ngOnInit();

    expect(store.dispatch).toHaveBeenCalledWith(
      loadEmployee({
        employeeId: 101
      })
    );
  });

  it('should expose selected employee observable', (done) => {
    component.employee$.subscribe(result => {
      expect(result).toEqual(employee);
      done();
    });
  });

  it('should expose loading observable', (done) => {
    component.loading$.subscribe(result => {
      expect(result).toBeFalse();
      done();
    });
  });

  it('should expose error observable', (done) => {
    component.error$.subscribe(result => {
      expect(result).toBeNull();
      done();
    });
  });

  it('should navigate back to employee details', () => {
    component.employeeId = 101;

    component.goBack();

    expect(router.navigate).toHaveBeenCalledWith(['/employees', 101]);
  });

  it('should navigate to employee list when route id is invalid', () => {
    const activatedRoute = TestBed.inject(
      ActivatedRoute
    ) as unknown as {
      snapshot: {
        paramMap: {
          get: jasmine.Spy;
        };
      };
    };

    activatedRoute.snapshot.paramMap.get.and.returnValue('0');

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(['/employees']);
    expect(store.dispatch).not.toHaveBeenCalled();
  });

  it('should navigate to employee list when route id is missing', () => {
    const activatedRoute = TestBed.inject(
      ActivatedRoute
    ) as unknown as {
      snapshot: {
        paramMap: {
          get: jasmine.Spy;
        };
      };
    };

    activatedRoute.snapshot.paramMap.get.and.returnValue(null);

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(['/employees']);
    expect(store.dispatch).not.toHaveBeenCalled();
  });

  it('should navigate to employee list when route id is negative', () => {
    const activatedRoute = TestBed.inject(ActivatedRoute) as unknown as {
      snapshot: {
        paramMap: {
          get: jasmine.Spy;
        };
      };
    };

    activatedRoute.snapshot.paramMap.get.and.returnValue('-5');

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(['/employees']);
    expect(store.dispatch).not.toHaveBeenCalled();
  })
})