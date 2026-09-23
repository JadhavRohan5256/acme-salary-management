import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';

import { loadEmployee } from '../../../store/employees/employee.actions';
import { selectSelectedEmployee, selectSelectedEmployeeLoading, selectSelectedEmployeeError } from '../../../store/employees/employee.selectors';
import { Employee } from '../../../core/models/employee';
import { loadCurrencies } from '../../../store/reference-data/reference-data.actions';

@Component({
  selector: 'app-update-salary-page',
  templateUrl: './update-salary-page.component.html',
  styleUrls: ['./update-salary-page.component.scss']
})
export class UpdateSalaryPageComponent implements OnInit {
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

    this.store.dispatch(loadEmployee({ employeeId: this.employeeId }));
    this.store.dispatch(loadCurrencies());
  }

  goBack(): void {
    this.router.navigate(['/employees', this.employeeId]);
  }
}
