import { createAction, props } from '@ngrx/store';
import { EmployeeQuery } from '../../core/services/employee/employee.service';
import { Employee } from '../../core/models/employee';
import { PageResponse } from '../../core/models/page';
import { SalaryHistory } from '../../core/models/salary-history';
import { SalaryUpdateRequest, SalaryUpdateResponse } from '../../core/models/salary-update';


export const loadEmployees = createAction(
  '[Employee] Load Employees',
  props<{ query: EmployeeQuery }>()
);

export const loadEmployeesSuccess = createAction(
  '[Employee] Load Employees Success',
  props<{ response: PageResponse<Employee> }>()
);

export const loadEmployeesFailure = createAction(
  '[Employee] Load Employees Failure',
  props<{ error: string }>()
);

export const loadEmployee = createAction(
  '[Employee] Load Employee',
  props<{ employeeId: number }>()
);

export const loadEmployeeSuccess = createAction(
  '[Employee] Load Employee Success',
  props<{ employee: Employee }>()
);

export const loadEmployeeFailure = createAction(
  '[Employee] Load Employee Failure',
  props<{ error: string }>()
);

export const loadSalaryHistory = createAction(
  '[Employee] Load Salary History',
  props<{ employeeId: number }>()
);

export const loadSalaryHistorySuccess = createAction(
  '[Employee] Load Salary History Success',
  props<{ salaryHistory: SalaryHistory[] }>()
);

export const loadSalaryHistoryFailure = createAction(
  '[Employee] Load Salary History Failure',
  props<{ error: string }>()
);

export const updateSalary = createAction(
  '[Employee] Update Salary',
  props<{ employeeId: number, request: SalaryUpdateRequest }>()
);

export const updateSalarySuccess = createAction(
  '[Employee] Update Salary Success',
  props<{ response: SalaryUpdateResponse }>()
);

export const updateSalaryFailure = createAction(
  '[Employee] Update Salary Failure',
  props<{ error: string }>()
);