import {
  AnalyticsOverview,
  CountryAnalytics,
  DepartmentAnalytics,
  SalaryDistribution,
  SalaryDistributionQuery
} from '../../core/models/analytics';

export interface AnalyticsState {
  overview: AnalyticsOverview | null;
  byCountry: CountryAnalytics[];
  byDepartment: DepartmentAnalytics[];
  salaryDistribution: SalaryDistribution[];
  selectedSalaryDistributionQuery: SalaryDistributionQuery | null;
  overviewLoading: boolean;
  countryLoading: boolean;
  departmentLoading: boolean;
  salaryDistributionLoading: boolean;
  overviewError: string | null;
  countryError: string | null;
  departmentError: string | null;
  salaryDistributionError: string | null;
}

export const initialAnalyticsState: AnalyticsState = {
  overview: null,
  byCountry: [],
  byDepartment: [],
  salaryDistribution: [],
  selectedSalaryDistributionQuery: null,
  overviewLoading: false,
  countryLoading: false,
  departmentLoading: false,
  salaryDistributionLoading: false,
  overviewError: null,
  countryError: null,
  departmentError: null,
  salaryDistributionError: null
};
