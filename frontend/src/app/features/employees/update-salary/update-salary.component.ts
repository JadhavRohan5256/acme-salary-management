import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';

import { updateSalary } from '../../../store/employees/employee.actions';
import { selectUpdatingSalary, selectUpdateSalaryError, selectUpdateSalarySuccess } from '../../../store/employees/employee.selectors';
import { selectCurrencies, selectCurrenciesLoading } from '../../../store/reference-data/reference-data.selectors';
import { Currency } from '../../../core/models/currency';

@Component({
  selector: 'app-update-salary',
  templateUrl: './update-salary.component.html',
  styleUrls: ['./update-salary.component.scss']
})
export class UpdateSalaryComponent implements OnChanges {
  @Input() employeeId!: number;

  salaryForm!: FormGroup;
  currencies$: Observable<Currency[]>;
  currenciesLoading$: Observable<boolean>;
  updating$: Observable<boolean>;
  error$: Observable<string | null>;
  success$: Observable<boolean>;

  constructor(
    private readonly fb: FormBuilder,
    private readonly store: Store
  ) {
    this.currencies$ = this.store.select(selectCurrencies);
    this.currenciesLoading$ = this.store.select(selectCurrenciesLoading);
    this.updating$ = this.store.select(selectUpdatingSalary);
    this.error$ = this.store.select(selectUpdateSalaryError);
    this.success$ = this.store.select(selectUpdateSalarySuccess);
    this.initializeFormGroup();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['employeeId'] && this.employeeId && this.employeeId > 0) {
      this.resetForm();
    }
  }

  initializeFormGroup() {
    this.salaryForm = this.fb.group({
      newSalary: [null,[Validators.required, Validators.min(0.01)]],
      currencyId: [null, Validators.required],
      effectiveDate: [null, Validators.required]
    })
  }

  submit(): void {
    if (this.salaryForm.invalid) {
      this.salaryForm.markAllAsTouched();
      return;
    }

    const { newSalary, currencyId, effectiveDate } = this.salaryForm.value;
    const payload = {
      employeeId: this.employeeId,
      request: {
        newSalary: Number(newSalary),
        currencyId: Number(currencyId),
        effectiveDate: this.formatDate(effectiveDate)
      }
    }

    this.store.dispatch(updateSalary(payload));
  }

  resetForm(): void {
    this.salaryForm.reset({
      newSalary: null,
      currencyId: null,
      effectiveDate: null
    });
  }

  private formatDate(date: Date | string): string {
    if (typeof date === 'string') {
      return date;
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  get newSalaryControl() {
    return this.salaryForm.get('newSalary');
  }

  get currencyIdControl() {
    return this.salaryForm.get('currencyId');
  }

  get effectiveDateControl() {
    return this.salaryForm.get('effectiveDate');
  }
}
