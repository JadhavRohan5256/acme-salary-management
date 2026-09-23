import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';

import { UpdateSalaryComponent } from './update-salary.component';
import { updateSalary } from '../../../store/employees/employee.actions';

describe('UpdateSalaryComponent', () => {
  let component: UpdateSalaryComponent;
  let fixture: ComponentFixture<UpdateSalaryComponent>;
  let store: jasmine.SpyObj<Store>;

  beforeEach(async () => {
    store = jasmine.createSpyObj<Store>(
      'Store',
      [
        'select',
        'dispatch'
      ]
    );

    store.select.and.callFake(() => of([]));

    await TestBed.configureTestingModule({
      declarations: [UpdateSalaryComponent],
      providers: [
        FormBuilder,
        {
          provide: Store,
          useValue: store
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(UpdateSalaryComponent);
    component = fixture.componentInstance;
    component.employeeId = 1;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form', () => {
    expect(component.salaryForm).toBeTruthy();
    expect(component.salaryForm.get('newSalary')).toBeTruthy();
    expect(component.salaryForm.get('currencyId')).toBeTruthy();
    expect(component.salaryForm.get('effectiveDate')).toBeTruthy();
  });

  it('should mark form as invalid initially', () => {
    expect(component.salaryForm.invalid).toBeTrue();
  });

  it('should not dispatch when form is invalid', () => {
    store.dispatch.calls.reset();
    component.submit();
    expect(store.dispatch).not.toHaveBeenCalled();
  });

  it('should dispatch updateSalary with valid form data', () => {
    store.dispatch.calls.reset();
    component.salaryForm.setValue({
      newSalary: 85000,
      currencyId: 1,
      effectiveDate: new Date(
        2026,
        8,
        24
      )
    });

    component.submit();
    expect(store.dispatch).toHaveBeenCalledWith(
      updateSalary({
        employeeId: 1,
        request: {
          newSalary: 85000,
          currencyId: 1,
          effectiveDate: '2026-09-24'
        }
      })
    );
  });

  it('should convert salary and currency values to numbers', () => {
    store.dispatch.calls.reset();
    component.salaryForm.setValue({
      newSalary: '95000',
      currencyId: '2',
      effectiveDate: '2026-09-25'
    });
    component.submit();

    expect(store.dispatch).toHaveBeenCalledWith(
      updateSalary({
        employeeId: 1,
        request: {
          newSalary: 95000,
          currencyId: 2,
          effectiveDate: '2026-09-25'
        }
      })
    );
  });

  it('should reset the form', () => {
    component.salaryForm.setValue({
      newSalary: 85000,
      currencyId: 1,
      effectiveDate: '2026-09-24'
    });

    component.resetForm();

    expect(component.salaryForm.value).toEqual({
      newSalary: null,
      currencyId: null,
      effectiveDate: null
    });
  });

  it('should validate minimum salary', () => {
    component.salaryForm.patchValue({
      newSalary: 0
    });

    expect(component.newSalaryControl?.hasError('min')).toBeTrue();
  });

  it('should require currency', () => {
    component.salaryForm.patchValue({
      currencyId: null
    });

    expect(component.currencyIdControl?.hasError('required')).toBeTrue();
  });

  it('should require effective date', () => {
    component.salaryForm.patchValue({
      effectiveDate: null
    });

    expect(component.effectiveDateControl?.hasError('required')).toBeTrue();
  });
});
