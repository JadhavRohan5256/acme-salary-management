import { createReducer, on } from '@ngrx/store';
import { initialEmployeeState } from './employee.state';
import {
  loadEmployees,
  loadEmployeesSuccess,
  loadEmployeesFailure,

  loadEmployee,
  loadEmployeeSuccess,
  loadEmployeeFailure,

  loadSalaryHistory,
  loadSalaryHistorySuccess,
  loadSalaryHistoryFailure,

  updateSalary,
  updateSalarySuccess,
  updateSalaryFailure
} from './employee.actions';

export const employeeReducer = createReducer(
  initialEmployeeState,

  on(loadEmployees, state => ({
    ...state,
    loading: true,
    error: null
  })),

  on(loadEmployeesSuccess, (state, { response }) => ({
    ...state,
    employees: response,
    loading: false,
    error: null
  })),

  on(loadEmployeesFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(loadEmployee, state => ({
    ...state,
    selectedEmployeeLoading: true,
    selectedEmployeeError: null
  })),

  on(loadEmployeeSuccess, (state, { employee }) => ({
    ...state,
    selectedEmployee: employee,
    selectedEmployeeLoading: false,
    selectedEmployeeError: null
  })),

  on(loadEmployeeFailure, (state, { error }) => ({
    ...state,
    selectedEmployeeLoading: false,
    selectedEmployeeError: error
  })),

  on(loadSalaryHistory, state => ({
    ...state,
    salaryHistoryLoading: true,
    salaryHistoryError: null
  })),

  on(loadSalaryHistorySuccess, (state, { salaryHistory }) => ({
    ...state,
    salaryHistory,
    salaryHistoryLoading: false,
    salaryHistoryError: null
  })),

  on(loadSalaryHistoryFailure, (state, { error }) => ({
    ...state,
    salaryHistoryLoading: false,
    salaryHistoryError: error
  })),

  on(updateSalary, state => ({
    ...state,
    updatingSalary: true,
    updateSalarySuccess: false,
    updateSalaryError: null
  })),

  on(updateSalarySuccess, (state, { response }) => ({
    ...state,
    updatingSalary: false,
    updateSalarySuccess: true,
    updateSalaryError: null,

    selectedEmployee: state.selectedEmployee
        ?{
            ...state.selectedEmployee,
            currentSalary: response.newSalary,
            currency: state.selectedEmployee.currency
        }
        :null
  })),

  on(updateSalaryFailure, (state, { error }) => ({
    ...state,
    updatingSalary: false,
    updateSalarySuccess: false,
    updateSalaryError: error
  }))
);