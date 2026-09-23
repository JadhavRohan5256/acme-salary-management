export interface SalaryUpdateRequest {
  newSalary: number;
  currencyId: number;
  effectiveDate: string;
}

export interface SalaryUpdateResponse {
  employeeId: number;
  previousSalary: number;
  newSalary: number;
  currencyId: number;
  effectiveDate: string;
  message: string;
}