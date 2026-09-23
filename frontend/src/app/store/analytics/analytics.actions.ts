import { createAction, props } from '@ngrx/store';

import {
  AnalyticsOverview,
  CountryAnalytics,
  DepartmentAnalytics,
  SalaryDistribution,
  SalaryDistributionQuery
} from '../../core/models/analytics';

export const loadOverview = createAction(
  '[Analytics] Load Overview'
);

export const loadOverviewSuccess = createAction(
  '[Analytics] Load Overview Success',
  props<{ overview: AnalyticsOverview }>()
);

export const loadOverviewFailure = createAction(
  '[Analytics] Load Overview Failure',
  props<{ error: string }>()
);

export const loadByCountry = createAction(
  '[Analytics] Load By Country'
);

export const loadByCountrySuccess = createAction(
  '[Analytics] Load By Country Success',
  props<{ data: CountryAnalytics[] }>()
);

export const loadByCountryFailure = createAction(
  '[Analytics] Load By Country Failure',
  props<{ error: string }>()
);

export const loadByDepartment = createAction(
  '[Analytics] Load By Department'
);

export const loadByDepartmentSuccess = createAction(
  '[Analytics] Load By Department Success',
  props<{ data: DepartmentAnalytics[] }>()
);

export const loadByDepartmentFailure = createAction(
  '[Analytics] Load By Department Failure',
  props<{ error: string }>()
);

export const loadSalaryDistribution = createAction(
  '[Analytics] Load Salary Distribution',
  props<{ query: SalaryDistributionQuery }>()
);

export const loadSalaryDistributionSuccess = createAction(
  '[Analytics] Load Salary Distribution Success',
  props<{ data: SalaryDistribution[] }>()
);

export const loadSalaryDistributionFailure = createAction(
  '[Analytics] Load Salary Distribution Failure',
  props<{ error: string }>()
);
