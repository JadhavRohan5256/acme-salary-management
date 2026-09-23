import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { CountryAnalytics } from '../../../core/models/analytics';
import { Store } from '@ngrx/store';
import { selectByCountry, selectCountryError, selectCountryLoading } from '../../../store/analytics/analytics.selectors';

@Component({
  selector: 'app-country-analytics',
  templateUrl: './country-analytics.component.html',
  styleUrl: './country-analytics.component.scss'
})
export class CountryAnalyticsComponent {
  countryAnalytics$: Observable<CountryAnalytics[]>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  displayedColumns: string[] = [
    'country',
    'employeeCount',
    'currency',
    'averageSalary',
    'minimumSalary',
    'maximumSalary'
  ];

  constructor(private readonly store: Store) {
    this.countryAnalytics$ = this.store.select(selectByCountry);
    this.loading$ = this.store.select(selectCountryLoading);
    this.error$ = this.store.select(selectCountryError);
  }
}
