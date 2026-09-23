import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';

import { AnalyticsComponent } from './analytics.component';
import { loadOverview, loadByCountry, loadByDepartment } from '../../store/analytics/analytics.actions';

import {
  selectOverview,
  selectByCountry,
  selectByDepartment,
  selectOverviewLoading,
  selectCountryLoading,
  selectDepartmentLoading,
  selectOverviewError,
  selectCountryError,
  selectDepartmentError
} from '../../store/analytics/analytics.selectors';


describe('AnalyticsComponent', () => {
  let component: AnalyticsComponent;
  let fixture: ComponentFixture<AnalyticsComponent>;
  let store: jasmine.SpyObj<Store>;

  beforeEach(async () => {
    store = jasmine.createSpyObj<Store>('Store', ['select', 'dispatch']);

    store.select.and.callFake((selector: unknown) => {
      if (selector === selectOverview) {
        return of({
          totalEmployees: 10000,
          totalCountries: 10,
          totalDepartments: 8
        });
      }

      if (selector === selectByCountry) {
        return of([]);
      }

      if (selector === selectByDepartment) {
        return of([]);
      }

      if (selector === selectOverviewLoading) {
        return of(false);
      }

      if (selector === selectCountryLoading) {
        return of(false);
      }

      if (selector === selectDepartmentLoading) {
        return of(false);
      }

      if (selector === selectOverviewError) {
        return of(null);
      }

      if (selector === selectCountryError) {
        return of(null);
      }

      if (selector === selectDepartmentError) {
        return of(null);
      }

      return of(null);
    });

    await TestBed.configureTestingModule({
      declarations: [AnalyticsComponent],
      imports: [ReactiveFormsModule],
      providers: [
        {
          provide: Store,
          useValue: store
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(AnalyticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize analytics observables', () => {
    expect(component.overview$).toBeTruthy();
    expect(component.countryAnalytics$).toBeTruthy();
    expect(component.departmentAnalytics$).toBeTruthy();

    expect(component.overviewLoading$).toBeTruthy();
    expect(component.countryLoading$).toBeTruthy();
    expect(component.departmentLoading$).toBeTruthy();

    expect(component.overviewError$).toBeTruthy();
    expect(component.countryError$).toBeTruthy();
    expect(component.departmentError$).toBeTruthy();
  });

  it('should initialize displayed columns', () => {
    expect(component.countryDisplayedColumns).toEqual([
      'country',
      'employeeCount',
      'currency',
      'averageSalary',
      'minimumSalary',
      'maximumSalary'
    ]);

    expect(component.departmentDisplayedColumns).toEqual([
      'department',
      'employeeCount',
      'currency',
      'averageSalary'
    ]);
  });

  it('should load all analytics on initialization', () => {
    expect(store.dispatch).toHaveBeenCalledWith(loadOverview());
    expect(store.dispatch).toHaveBeenCalledWith(loadByCountry());
    expect(store.dispatch).toHaveBeenCalledWith(loadByDepartment());
  });

  it('should dispatch all analytics actions when refreshing', () => {
    store.dispatch.calls.reset();

    component.refresh();

    expect(store.dispatch).toHaveBeenCalledWith(loadOverview());
    expect(store.dispatch).toHaveBeenCalledWith(loadByCountry());
    expect(store.dispatch).toHaveBeenCalledWith(loadByDepartment());
  });

  it('should dispatch all analytics actions from loadAnalytics', () => {
    store.dispatch.calls.reset();
    component.loadAnalytics();
    expect(store.dispatch).toHaveBeenCalledTimes(3);
  });

  it('should expose overview data', (done) => {
    component.overview$.subscribe(overview => {
      expect(overview?.totalEmployees).toBe(10000);
      expect(overview?.totalCountries).toBe(10);
      expect(overview?.totalDepartments).toBe(8);
      done();
    });
  });

  it('should expose country analytics', (done) => {
    component.countryAnalytics$.subscribe(data => {
      expect(data).toEqual([]);
      done();
    });
  });

  it('should expose department analytics', (done) => {
    component.departmentAnalytics$.subscribe(data => {
      expect(data).toEqual([]);
      done();
    });
  });
});
