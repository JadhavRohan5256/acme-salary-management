import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';

import {
  selectSalaryHistory,
  selectSalaryHistoryLoading,
  selectSalaryHistoryError
} from '../../../store/employees/employee.selectors';
import { loadSalaryHistory } from '../../../store/employees/employee.actions';
import { SalaryHistory } from '../../../core/models/salary-history';

@Component({
  selector: 'app-salary-history',
  templateUrl: './salary-history.component.html',
  styleUrls: ['./salary-history.component.scss']
})
export class SalaryHistoryComponent implements OnChanges {
  @Input() employeeId!: number;

  salaryHistory$: Observable<SalaryHistory[]>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;
  displayedColumns: string[] = [
    'previousSalary',
    'newSalary',
    'currency',
    'effectiveDate',
    'changedBy',
    'createdAt'
  ];

  constructor(
    private readonly store: Store
  ) {
    this.salaryHistory$ = this.store.select(selectSalaryHistory);
    this.loading$ = this.store.select(selectSalaryHistoryLoading);
    this.error$ = this.store.select(selectSalaryHistoryError);
  }

  ngOnChanges(changes: SimpleChanges): void {
    const employeeIdChange = changes['employeeId'];

    if (employeeIdChange && this.employeeId && this.employeeId > 0) {
      this.loadHistory();
    }
  }

  loadHistory(): void {
    this.store.dispatch(loadSalaryHistory({ employeeId: this.employeeId }));
  }
}
