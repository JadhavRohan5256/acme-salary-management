import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, of, switchMap } from 'rxjs';
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
import { SalaryUpdateResponse } from '../../core/models/salary-update';
import { SalaryHistory } from '../../core/models/salary-history';
import { Employee } from '../../core/models/employee';
import { EmployeeService } from '../../core/services/employee/employee.service';

@Injectable()
export class EmployeeEffects {
  loadEmployees$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadEmployees),
      switchMap(({ query }) =>
        this.employeeService.getEmployees(query).pipe(
            map(response => loadEmployeesSuccess({ response })),
            catchError(error =>
              of(
                loadEmployeesFailure({
                  error: error?.error?.message ?? 'Failed to load employees'
                })
              )
            )
          )
      )
    )
  );

  loadEmployee$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadEmployee),
      switchMap(({ employeeId }) =>
        this.employeeService.getEmployee(employeeId).pipe(
            map((employee: Employee) => loadEmployeeSuccess({ employee })),
            catchError(error =>
              of(
                loadEmployeeFailure({
                  error: error?.error?.message ?? 'Failed to load employee'
                })
              )
            )
          )
      )
    )
  );

  loadSalaryHistory$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadSalaryHistory),
      switchMap(({ employeeId }) =>
        this.employeeService.getSalaryHistory(employeeId).pipe(
            map((salaryHistory: SalaryHistory[]) => loadSalaryHistorySuccess({salaryHistory})),
            catchError(error =>
              of(
                loadSalaryHistoryFailure({
                  error: error?.error?.message ?? 'Failed to load salary history'
                })
              )
            )
          )
      )
    )
  );

  updateSalary$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateSalary),

      switchMap(
        ({ employeeId, request }) =>
          this.employeeService.updateSalary(employeeId, request).pipe(
              map((response: SalaryUpdateResponse)  => updateSalarySuccess({ response })),
              catchError(error =>
                of(
                  updateSalaryFailure({
                    error:
                      error?.error?.message ??
                      'Failed to update salary'
                  })
                )
              )
            )
      )
    )
  );

  constructor(
    private readonly actions$: Actions,
    private readonly employeeService: EmployeeService
  ) {}
}