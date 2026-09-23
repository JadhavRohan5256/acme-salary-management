import { createFeatureSelector, createSelector } from '@ngrx/store';
import { AnalyticsState } from './analytics.state';


export const selectAnalyticsState = createFeatureSelector<AnalyticsState>('analytics');

export const selectOverview = createSelector(
  selectAnalyticsState,
  state => state.overview
);

export const selectTotalEmployees = createSelector(
  selectOverview,
  overview => overview?.totalEmployees ?? 0
);

export const selectTotalCountries = createSelector(
  selectOverview,
  overview => overview?.totalCountries ?? 0
);

export const selectTotalDepartments = createSelector(
  selectOverview,
  overview => overview?.totalDepartments ?? 0
);

export const selectOverviewLoading = createSelector(
  selectAnalyticsState,
  state => state.overviewLoading
);

export const selectOverviewError = createSelector(
  selectAnalyticsState,
  state => state.overviewError
);

export const selectByCountry = createSelector(
  selectAnalyticsState,
  state => state.byCountry
);

export const selectCountryLoading = createSelector(
  selectAnalyticsState,
  state => state.countryLoading
);

export const selectCountryError = createSelector(
  selectAnalyticsState,
  state => state.countryError
);

export const selectByDepartment = createSelector(
  selectAnalyticsState,
  state => state.byDepartment
);

export const selectDepartmentLoading = createSelector(
  selectAnalyticsState,
  state => state.departmentLoading
);

export const selectDepartmentError = createSelector(
  selectAnalyticsState,
  state => state.departmentError
);

export const selectSalaryDistribution = createSelector(
  selectAnalyticsState,
  state => state.salaryDistribution
);

export const selectSalaryDistributionQuery = createSelector(
  selectAnalyticsState,
  state => state.selectedSalaryDistributionQuery
);

export const selectSalaryDistributionLoading = createSelector(
  selectAnalyticsState,
  state => state.salaryDistributionLoading
);

export const selectSalaryDistributionError = createSelector(
  selectAnalyticsState,
  state => state.salaryDistributionError
);
