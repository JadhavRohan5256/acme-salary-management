import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup} from '@angular/forms';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { loadEmployees } from '../../../store/employees/employee.actions';

import { selectEmployees, selectEmployeesLoading, selectEmployeesError } from '../../../store/employees/employee.selectors';
import { PageResponse } from '../../../core/models/page';
import { Employee } from '../../../core/models/employee';
import { selectCountries, selectCountriesLoading } from '../../../store/reference-data/reference-data.selectors';
import { Country } from '../../../core/models/country';
import { loadCountries, loadCurrencies } from '../../../store/reference-data/reference-data.actions';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees$: Observable<PageResponse<Employee> | null>;
  loading$: Observable<boolean>;
  countriesLoading$: Observable<boolean>;
  countries$: Observable<Country[]>;
  error$: Observable<string | null>;
  filterForm!: FormGroup;
  displayedColumns: string[] = [
    'name',
    'email',
    'country',
    'department',
    'designation',
    'salary',
    'actions'
  ];

  constructor(
    private readonly store: Store,
    private readonly fb: FormBuilder
  ) {
    this.employees$ = this.store.select(selectEmployees);
    this.loading$ = this.store.select(selectEmployeesLoading);
    this.error$ = this.store.select(selectEmployeesError);
    this.countries$ = this.store.select(selectCountries); 
    this.countriesLoading$ = this.store.select(selectCountriesLoading);
  }

  ngOnInit(): void {
    this.store.dispatch(loadCountries());
    this.initializeFormGroup();
    this.loadEmployees();
  }

  private initializeFormGroup() {
    this.filterForm = this.fb.group({
      search: [''],
      countryId: [null],
      department: ['']
    });
  }

  loadEmployees(): void {
    const { search, countryId, department } = this.filterForm.value;
    const payload = {
      query: {
        page: 0,
        size: 20,
        search: search || undefined,
        countryId: countryId || undefined,
        department: department || undefined
      }
    }

    this.store.dispatch(loadEmployees(payload));
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: '',
      countryId: null,
      department: ''
    });

    this.loadEmployees();
  }

  onPageChange(event: any): void {
    const { search, countryId, department } = this.filterForm.value;
    const payload = {
      query: {
        page: event.pageIndex,
        size: event.pageSize,
        search: search || undefined,
        countryId: countryId || undefined,
        department: department || undefined
      }
    }

    this.store.dispatch(loadEmployees(payload));
  }


  handleFormSubmit(): void {
    this.loadEmployees();
  }
}