import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { DepartmentAnalytics } from '../../../core/models/analytics';
import { Store } from '@ngrx/store';
import { selectByDepartment, selectDepartmentError, selectDepartmentLoading } from '../../../store/analytics/analytics.selectors';

@Component({
  selector: 'app-department-analytics',
  templateUrl: './department-analytics.component.html',
  styleUrl: './department-analytics.component.scss'
})
export class DepartmentAnalyticsComponent {
  departmentAnalytics$: Observable<DepartmentAnalytics[]>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  displayedColumns: string[] = [
    'department',
    'employeeCount',
    'currency',
    'averageSalary'
  ];

  constructor(private readonly store: Store) {
    this.departmentAnalytics$ = this.store.select(selectByDepartment);
    this.loading$ = this.store.select(selectDepartmentLoading);
    this.error$ = this.store.select(selectDepartmentError);
  }
}
