import { createFeatureSelector, createSelector } from '@ngrx/store';
import { EmployeeState } from './employee.state';



export const selectEmployeeState = createFeatureSelector<EmployeeState>('employees');

export const selectEmployees = createSelector(
  selectEmployeeState,
  state => state.employees
);

export const selectEmployeeList = createSelector(
  selectEmployees,
  employees => employees?.content ?? []
);

export const selectEmployeePage = createSelector(
  selectEmployees,
  employees => employees
);

export const selectSelectedEmployee = createSelector(
    selectEmployeeState,
    state => state.selectedEmployee
);

export const selectSelectedEmployeeLoading = createSelector(
    selectEmployeeState,
    state => state.selectedEmployeeLoading
);

export const selectSelectedEmployeeError = createSelector(
    selectEmployeeState,
    state => state.selectedEmployeeError
);

export const selectSalaryHistory = createSelector(
    selectEmployeeState,
    state => state.salaryHistory
);

export const selectSalaryHistoryLoading = createSelector(
    selectEmployeeState,
    state => state.salaryHistoryLoading
);

export const selectSalaryHistoryError = createSelector(
    selectEmployeeState,
    state => state.salaryHistoryError
);

export const selectEmployeesLoading = createSelector(
    selectEmployeeState,
    state => state.loading
);

export const selectEmployeesError = createSelector(
    selectEmployeeState,
    state => state.error
);

export const selectUpdatingSalary = createSelector(
    selectEmployeeState,
    state => state.updatingSalary
);

export const selectUpdateSalarySuccess = createSelector(
    selectEmployeeState,
    state => state.updateSalarySuccess
);

export const selectUpdateSalaryError = createSelector(
    selectEmployeeState,
    state => state.updateSalaryError
);