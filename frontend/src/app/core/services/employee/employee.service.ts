import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { PageResponse } from '../../models/page';
import { Employee } from '../../models/employee';
import { SalaryHistory } from '../../models/salary-history';
import { SalaryUpdateRequest, SalaryUpdateResponse } from '../../models/salary-update';


export interface EmployeeQuery {
  page?: number;
  size?: number;
  search?: string;
  countryId?: number;
  department?: string;
  sort?: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private readonly apiUrl = `${environment.apiUrl}/employees`;

  constructor(private readonly http: HttpClient) {}

  getEmployees(query: EmployeeQuery = {}): Observable<PageResponse<Employee>> {
    let params = new HttpParams();

    if (query.page !== undefined) {
      params = params.set('page', query.page);
    }

    if (query.size !== undefined) {
      params = params.set('size', query.size);
    }

    if (query.search) {
      params = params.set('search', query.search);
    }

    if (query.countryId !== undefined) {
      params = params.set('countryId', query.countryId);
    }

    if (query.department) {
      params = params.set('department', query.department);
    }

    if (query.sort) {
      params = params.set('sort', query.sort);
    }

    return this.http.get<PageResponse<Employee>>(
      this.apiUrl,
      { params }
    );
  }

  getEmployee(employeeId: number): Observable<Employee> {
    return this.http.get<Employee>(
      `${this.apiUrl}/${employeeId}`
    );
  }

  getSalaryHistory(employeeId: number): Observable<SalaryHistory[]> {
    return this.http.get<SalaryHistory[]>(
      `${this.apiUrl}/${employeeId}/salary-history`
    );
  }

  updateSalary(employeeId: number, request: SalaryUpdateRequest): Observable<SalaryUpdateResponse> {
    return this.http.put<SalaryUpdateResponse>(
      `${this.apiUrl}/${employeeId}/salary`,
      request
    );
  }
}