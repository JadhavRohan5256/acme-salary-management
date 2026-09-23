import { createReducer, on } from '@ngrx/store';

import {
  loadOverview,
  loadOverviewSuccess,
  loadOverviewFailure,
  loadByCountry,
  loadByCountrySuccess,
  loadByCountryFailure,
  loadByDepartment,
  loadByDepartmentSuccess,
  loadByDepartmentFailure,
  loadSalaryDistribution,
  loadSalaryDistributionSuccess,
  loadSalaryDistributionFailure
} from './analytics.actions';
import { initialAnalyticsState } from './analytics.state';

export const analyticsReducer = createReducer(
  initialAnalyticsState,

  on(loadOverview, state => ({
    ...state,
    overviewLoading: true,
    overviewError: null
  })),

  on(loadOverviewSuccess, (state, { overview }) => ({
    ...state,
    overview,
    overviewLoading: false,
    overviewError: null
  })),

  on(loadOverviewFailure, (state, { error }) => ({
    ...state,
    overviewLoading: false,
    overviewError: error
  })),

  on(loadByCountry, state => ({
    ...state,
    countryLoading: true,
    countryError: null
  })),

  on(loadByCountrySuccess, (state, { data }) => ({
    ...state,
    byCountry: data,
    countryLoading: false,
    countryError: null
  })),

  on(loadByCountryFailure, (state, { error }) => ({
    ...state,
    countryLoading: false,
    countryError: error
  })),

  on(loadByDepartment, state => ({
    ...state,
    departmentLoading: true,
    departmentError: null
  })),

  on(loadByDepartmentSuccess, (state, { data }) => ({
    ...state,
    byDepartment: data,
    departmentLoading: false,
    departmentError: null
  })),

  on(loadByDepartmentFailure, (state, { error }) => ({
    ...state,
    departmentLoading: false,
    departmentError: error
  })),

  on(loadSalaryDistribution, (state, { query }) => ({
    ...state,
    salaryDistributionLoading: true,
    salaryDistributionError: null,
    selectedSalaryDistributionQuery: query
  })),

  on(loadSalaryDistributionSuccess, (state, { data }) => ({
    ...state,
    salaryDistribution: data,
    salaryDistributionLoading: false,
    salaryDistributionError: null
  })),

  on(loadSalaryDistributionFailure, (state, { error }) => ({
    ...state,
    salaryDistributionLoading: false,
    salaryDistributionError: error
  }))
);
