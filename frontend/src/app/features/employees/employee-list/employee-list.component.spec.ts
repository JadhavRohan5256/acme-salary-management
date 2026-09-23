import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { EmployeeListComponent } from './employee-list.component';
import { loadEmployees } from '../../../store/employees/employee.actions';
import { PageResponse } from '../../../core/models/page';
import { Employee } from '../../../core/models/employee';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { selectEmployees, selectEmployeesError, selectEmployeesLoading } from '../../../store/employees/employee.selectors';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { loadCountries } from '../../../store/reference-data/reference-data.actions';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { selectCountries, selectCountriesLoading } from '../../../store/reference-data/reference-data.selectors';

describe('EmployeeListComponent', () => {
  let component: EmployeeListComponent;
  let fixture: ComponentFixture<EmployeeListComponent>;
  let store: MockStore;
  let dispatchSpy: jasmine.Spy;

  const employeesResponse: PageResponse<Employee> = {
    content: [
      {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        country: {
          id: 1,
          name: 'India',
          code: 'IN'
        },
        department: 'Engineering',
        designation: 'Software Engineer',
        currentSalary: 75000,
        currency: {
          id: 1,
          code: 'INR',
          name: 'Indian Rupee',
          symbol: '₹'
        }
      }
    ],
    page: 0,
    size: 20,
    totalElements: 1,
    totalPages: 1
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EmployeeListComponent],

      imports: [
        NoopAnimationsModule,
        ReactiveFormsModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatProgressSpinnerModule,
        MatCardModule,
        MatIconModule,
      ],
      providers: [
        provideMockStore({
          selectors: [
            {
              selector: selectEmployees,
              value: employeesResponse
            },
            {
              selector: selectEmployeesLoading,
              value: false
            },
            {
              selector: selectEmployeesError,
              value: null
            },
            {
              selector: selectCountries,
              value: []
            },
            {
              selector: selectCountriesLoading,
              value: false
            }
          ]
        })
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(EmployeeListComponent);
    component = fixture.componentInstance;

    store = TestBed.inject(MockStore);
    dispatchSpy = spyOn(store, 'dispatch');

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the filter form', () => {
    expect(component.filterForm).toBeTruthy();

    expect(component.filterForm.value).toEqual({
      search: '',
      countryId: null,
      department: ''
    });
  });

  it('should define displayed columns', () => {
    expect(component.displayedColumns).toEqual([
      'name',
      'email',
      'country',
      'department',
      'designation',
      'salary',
      'actions'
    ]);
  });

  it('should load employees on initialization', () => {
    expect(store.dispatch).toHaveBeenCalledWith(
      loadEmployees({
        query: {
          page: 0,
          size: 20,
          search: undefined,
          countryId: undefined,
          department: undefined
        }
      })
    );
  });

  it('should dispatch loadEmployees with filter values', () => {
    dispatchSpy.calls.reset();

    component.filterForm.patchValue({
      search: 'John',
      countryId: 1,
      department: 'Engineering'
    });

    component.loadEmployees();

    expect(store.dispatch).toHaveBeenCalledWith(
      loadEmployees({
        query: {
          page: 0,
          size: 20,
          search: 'John',
          countryId: 1,
          department: 'Engineering'
        }
      })
    );
  });

  it('should dispatch loadEmployees without empty filter values', () => {
    dispatchSpy.calls.reset();

    component.filterForm.patchValue({
      search: '',
      countryId: null,
      department: ''
    });

    component.loadEmployees();

    expect(store.dispatch).toHaveBeenCalledWith(
      loadEmployees({
        query: {
          page: 0,
          size: 20,
          search: undefined,
          countryId: undefined,
          department: undefined
        }
      })
    );
  });

  it('should clear filters and reload employees', () => {
    dispatchSpy.calls.reset();

    component.filterForm.patchValue({
      search: 'John',
      countryId: 1,
      department: 'Engineering'
    });

    component.clearFilters();

    expect(component.filterForm.value).toEqual({
      search: '',
      countryId: null,
      department: ''
    });

    expect(store.dispatch).toHaveBeenCalledWith(
      loadEmployees({
        query: {
          page: 0,
          size: 20,
          search: undefined,
          countryId: undefined,
          department: undefined
        }
      })
    );
  });

  it('should dispatch loadEmployees when page changes', () => {
    dispatchSpy.calls.reset();

    component.filterForm.patchValue({
      search: 'John',
      countryId: 1,
      department: 'Engineering'
    });

    component.onPageChange({
      pageIndex: 2,
      pageSize: 50
    });

    expect(store.dispatch).toHaveBeenCalledWith(
      loadEmployees({
        query: {
          page: 2,
          size: 50,
          search: 'John',
          countryId: 1,
          department: 'Engineering'
        }
      })
    );
  });

  it('should use the current filters when changing page', () => {
    dispatchSpy.calls.reset();

    component.filterForm.patchValue({
      search: 'Jane',
      countryId: 2,
      department: 'Finance'
    });

    component.onPageChange({
      pageIndex: 1,
      pageSize: 10
    });

    expect(store.dispatch).toHaveBeenCalledTimes(1);

    const dispatchedAction = dispatchSpy.calls.mostRecent().args[0] as ReturnType<typeof loadEmployees>;

    expect(dispatchedAction.query.page).toBe(1);
    expect(dispatchedAction.query.size).toBe(10);
    expect(dispatchedAction.query.search).toBe('Jane');
    expect(dispatchedAction.query.countryId).toBe(2);
    expect(dispatchedAction.query.department).toBe('Finance');
  });

  it('should expose employees observable', (done) => {
    component.employees$.subscribe(employees => {
      expect(employees).toEqual(employeesResponse);
      done();
    });
  });

  it('should expose loading observable', (done) => {
    component.loading$.subscribe(loading => {
      expect(loading).toBeFalse();
      done();
    });
  });

  it('should expose error observable', (done) => {
    component.error$.subscribe(error => {
      expect(error).toBeNull();
      done();
    });
  });

  it('should load countries on initialization', () => {
    expect(store.dispatch).toHaveBeenCalledWith(loadCountries());
  });

  it('should expose countries observable', (done) => {
    component.countries$.subscribe(countries => {
      expect(countries).toEqual([]);
      done();
    });
  });

  it('should expose countries loading observable', (done) => {
    component.countriesLoading$.subscribe(loading => {
      expect(loading).toBeFalse();
      done();
    });
  });

  it('should dispatch employee search with selected country', () => {
    dispatchSpy.calls.reset();

    component.filterForm.patchValue({
      search: '',
      countryId: 1,
      department: ''
    });

    component.loadEmployees();

    expect(store.dispatch).toHaveBeenCalledWith(
      loadEmployees({
        query: {
          page: 0,
          size: 20,
          search: undefined,
          countryId: 1,
          department: undefined
        }
      })
    );
  });
});
