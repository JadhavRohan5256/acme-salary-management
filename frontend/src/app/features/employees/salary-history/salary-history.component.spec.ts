import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';

import { SalaryHistoryComponent } from './salary-history.component';
import { loadSalaryHistory } from '../../../store/employees/employee.actions';
import { SalaryHistory } from '../../../core/models/salary-history';
import { selectSalaryHistory, selectSalaryHistoryLoading } from '../../../store/employees/employee.selectors';


describe('SalaryHistoryComponent', () => {
  let component: SalaryHistoryComponent;
  let fixture: ComponentFixture<SalaryHistoryComponent>;
  let store: jasmine.SpyObj<Store>;

  const salaryHistory: SalaryHistory[] = [
    {
      id: 1,
      previousSalary: 60000,
      newSalary: 75000,
      currency: {
        id: 1,
        code: 'INR',
        name: 'Indian Rupee',
        symbol: '₹'
      },
      effectiveDate: '2026-01-01',
      changedBy: 'hr.manager',
      createdAt: '2026-01-01T10:00:00'
    }
  ];

  beforeEach(async () => {
    store = jasmine.createSpyObj<Store>(
      'Store',
      [
        'select',
        'dispatch'
      ]
    );

    store.select.and.callFake((selector: any) => {
      if (selector === selectSalaryHistory) {
        return of(salaryHistory);
      }

      if (selector === selectSalaryHistoryLoading) {
        return of(true);
      }

      return of(null);
    });

    await TestBed.configureTestingModule({
      declarations: [ SalaryHistoryComponent ],
      providers: [
        {
          provide: Store,
          useValue: store
        }
      ],
      schemas: [ NO_ERRORS_SCHEMA ]
    }).compileComponents();

    fixture = TestBed.createComponent(SalaryHistoryComponent);
    component = fixture.componentInstance;
    component.employeeId = 1;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should define displayed columns', () => {
    expect(component.displayedColumns).toEqual([
      'previousSalary',
      'newSalary',
      'currency',
      'effectiveDate',
      'changedBy',
      'createdAt'
    ]);
  });

  it('should dispatch loadSalaryHistory when employee id changes', () => {
    component.ngOnChanges({
      employeeId: {
        currentValue: 1,
        previousValue: undefined,
        firstChange: true,
        isFirstChange: () => true
      }
    });

    expect(store.dispatch).toHaveBeenCalledWith(
      loadSalaryHistory({
        employeeId: 1
      })
    );
  });

  it('should load salary history manually', () => {
    store.dispatch.calls.reset();
    component.loadHistory();

    expect(store.dispatch).toHaveBeenCalledWith(
      loadSalaryHistory({
        employeeId: 1
      })
    );
  });

  it('should not load history for invalid employee id', () => {
    store.dispatch.calls.reset();
    component.employeeId = 0;

    component.ngOnChanges({
      employeeId: {
        currentValue: 0,
        previousValue: 1,
        firstChange: false,
        isFirstChange: () => false
      }
    });

    expect(store.dispatch).not.toHaveBeenCalled();
  });

  it('should expose salary history observable', (done) => {
    component.salaryHistory$.subscribe(result => {
      expect(result).toEqual(salaryHistory);
      done();
    });
  });

  it('should expose loading observable', (done) => {
    component.loading$.subscribe(result => {
      expect(result).toBeTrue();
      done();
    });
  });
});
