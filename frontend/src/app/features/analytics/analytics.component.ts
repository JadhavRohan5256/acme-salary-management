import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';

import { AnalyticsOverview, CountryAnalytics, DepartmentAnalytics } from '../../core/models/analytics';
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
import { Router } from '@angular/router';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.scss']
})
export class AnalyticsComponent implements OnInit {
  overview$: Observable<AnalyticsOverview | null>;
  countryAnalytics$: Observable<CountryAnalytics[]>;
  departmentAnalytics$: Observable<DepartmentAnalytics[]>;

  overviewLoading$: Observable<boolean>;
  countryLoading$: Observable<boolean>;
  departmentLoading$: Observable<boolean>;

  overviewError$: Observable<string | null>;
  countryError$: Observable<string | null>;
  departmentError$: Observable<string | null>;

  countryDisplayedColumns: string[] = [
    'country',
    'employeeCount',
    'currency',
    'averageSalary',
    'minimumSalary',
    'maximumSalary'
  ];

  departmentDisplayedColumns: string[] = [
    'department',
    'employeeCount',
    'currency',
    'averageSalary'
  ];

  constructor(
    private readonly store: Store,
    private readonly router: Router
  ) {
    this.overview$ = this.store.select(selectOverview);
    this.countryAnalytics$ = this.store.select(selectByCountry);
    this.departmentAnalytics$ = this.store.select(selectByDepartment);

    this.overviewLoading$ = this.store.select(selectOverviewLoading);
    this.countryLoading$ = this.store.select(selectCountryLoading);
    this.departmentLoading$ =this.store.select(selectDepartmentLoading);

    this.overviewError$ = this.store.select(selectOverviewError);
    this.countryError$ = this.store.select(selectCountryError);
    this.departmentError$ = this.store.select(selectDepartmentError);
  }

  ngOnInit(): void {
    this.loadAnalytics();
  }

  loadAnalytics(): void {
    this.store.dispatch(loadOverview());
    this.store.dispatch(loadByCountry());
    this.store.dispatch(loadByDepartment());
  }

  refresh(): void {
    this.loadAnalytics();
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
