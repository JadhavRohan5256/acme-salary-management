import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { provideMockStore, MockStore } from '@ngrx/store/testing';

import { DepartmentAnalyticsComponent } from './department-analytics.component';
import { selectByDepartment, selectDepartmentLoading, selectDepartmentError } from '../../../store/analytics/analytics.selectors';

describe('DepartmentAnalyticsComponent', () => {
  let component: DepartmentAnalyticsComponent;
  let fixture: ComponentFixture<DepartmentAnalyticsComponent>;
  let store: MockStore;

  const initialState = {
    analytics: {
      byDepartment: [],
      departmentLoading: false,
      departmentError: null
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DepartmentAnalyticsComponent],
      providers: [
        provideMockStore({
          initialState
        })
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    store = TestBed.inject(MockStore);

    fixture = TestBed.createComponent(DepartmentAnalyticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    store.resetSelectors();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should expose department analytics observable', (done) => {
    const data = [
      {
        department: 'Engineering',
        currency: {
          id: 1,
          code: 'INR',
          name: 'Indian Rupee',
          symbol: '₹'
        },
        employeeCount: 1500,
        averageSalary: 95000
      }
    ];

    store.overrideSelector(selectByDepartment, data);
    store.refreshState();

    component.departmentAnalytics$.subscribe(value => {
      expect(value).toEqual(data);
      done();
    });
  });

  it('should expose loading state', (done) => {
    store.overrideSelector(selectDepartmentLoading, true);
    store.refreshState();

    component.loading$.subscribe(value => {
      expect(value).toBeTrue();
      done();
    });
  });

  it('should expose error state', (done) => {
    const error = 'Failed to load department analytics';

    store.overrideSelector(selectDepartmentError, error);
    store.refreshState();

    component.error$.subscribe(value => {
      expect(value).toBe(error);
      done();
    });
  });

  it('should have correct displayed columns', () => {
    expect(component.displayedColumns).toEqual([
      'department',
      'employeeCount',
      'currency',
      'averageSalary'
    ]);
  });
});