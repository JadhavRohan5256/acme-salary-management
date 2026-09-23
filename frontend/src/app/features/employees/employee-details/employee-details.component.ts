import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';

import {
  selectSelectedEmployee,
  selectSelectedEmployeeLoading,
  selectSelectedEmployeeError
} from '../../../store/employees/employee.selectors';
import { Employee } from '../../../core/models/employee';
import { loadEmployee } from '../../../store/employees/employee.actions';

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.scss']
})
export class EmployeeDetailsComponent implements OnInit {
  employee$: Observable<Employee | null>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  employeeId!: number;

  constructor(
    private readonly store: Store,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {
    this.employee$ = this.store.select(selectSelectedEmployee);
    this.loading$ = this.store.select(selectSelectedEmployeeLoading);
    this.error$ = this.store.select(selectSelectedEmployeeError);
  }

  ngOnInit(): void {
    this.employeeId = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.employeeId || this.employeeId <= 0) {
      this.router.navigate(['/employees']);
      return;
    }

    this.store.dispatch(
      loadEmployee({ employeeId: this.employeeId })
    );
  }

  goBack(): void {
    this.router.navigate(['/employees']);
  }
}
