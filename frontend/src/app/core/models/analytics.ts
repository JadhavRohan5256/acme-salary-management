import { Country } from "./country";
import { Currency } from "./currency";

export interface AnalyticsOverview {
  totalEmployees: number;
  totalCountries: number;
  totalDepartments: number;
}

export interface CountryAnalytics {
  country: Country;
  employeeCount: number;
  currency: Currency;
  averageSalary: number;
  minimumSalary: number;
  maximumSalary: number;
}

export interface DepartmentAnalytics {
  department: string;
  currency: Currency;
  employeeCount: number;
  averageSalary: number;
}

export interface SalaryDistribution {
  range: string;
  employeeCount: number;
}

export interface SalaryDistributionQuery {
  currencyId: number;
  countryId?: number;
  department?: string;
}
