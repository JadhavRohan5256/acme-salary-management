import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { Observable } from 'rxjs';

import {
  AnalyticsOverview,
  CountryAnalytics,
  DepartmentAnalytics,
  SalaryDistribution,
  SalaryDistributionQuery
} from '../../models/analytics';
import { environment } from '../../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private readonly apiUrl = `${environment.apiUrl}/analytics`;

  constructor(
    private readonly http: HttpClient
  ) {}

  getOverview(): Observable<AnalyticsOverview> {
    return this.http.get<AnalyticsOverview>(
      `${this.apiUrl}/overview`
    );
  }

  getByCountry(): Observable<CountryAnalytics[]> {
    return this.http.get<CountryAnalytics[]>(
      `${this.apiUrl}/by-country`
    );
  }

  getByDepartment(): Observable<DepartmentAnalytics[]> {
    return this.http.get<DepartmentAnalytics[]>(
      `${this.apiUrl}/by-department`
    );
  }

  getSalaryDistribution(query: SalaryDistributionQuery): Observable<SalaryDistribution[]> {
    let params = new HttpParams().set('currencyId', query.currencyId);

    if (query.countryId !== undefined) {
      params = params.set('countryId', query.countryId);
    }

    if (query.department) {
      params = params.set('department', query.department);
    }

    return this.http.get<SalaryDistribution[]>(
      `${this.apiUrl}/salary-distribution`,
      { params }
    );
  }
}
