import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, of, switchMap } from 'rxjs';

import {
  loadOverview,
  loadOverviewSuccess,
  loadOverviewFailure,
  loadByCountry,
  loadByCountrySuccess,
  loadByCountryFailure,
  loadByDepartment,
  loadByDepartmentSuccess,
  loadByDepartmentFailure,
  loadSalaryDistribution,
  loadSalaryDistributionSuccess,
  loadSalaryDistributionFailure
} from './analytics.actions';
import { AnalyticsOverview, CountryAnalytics, DepartmentAnalytics, SalaryDistribution } from '../../core/models/analytics';
import { AnalyticsService } from '../../core/services/analytics/analytics.service';

@Injectable()
export class AnalyticsEffects {
  loadOverview$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadOverview),
      switchMap(() =>
        this.analyticsService.getOverview().pipe(
          map((overview: AnalyticsOverview) => loadOverviewSuccess({ overview })),
          catchError(error =>
            of(
              loadOverviewFailure({
                error: error?.error?.message ?? 'Failed to load analytics overview'
              })
            )
          )
        )
      )
    )
  );

  loadByCountry$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadByCountry),
      switchMap(() =>
        this.analyticsService.getByCountry().pipe(
          map((data: CountryAnalytics[]) => loadByCountrySuccess({ data })),
          catchError(error =>
            of(
              loadByCountryFailure({
                error: error?.error?.message ?? 'Failed to load country analytics'
              })
            )
          )
        )
      )
    )
  );

  loadByDepartment$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadByDepartment),
      switchMap(() =>
        this.analyticsService.getByDepartment().pipe(
          map((data: DepartmentAnalytics[]) => loadByDepartmentSuccess({ data })),
          catchError(error =>
            of(
              loadByDepartmentFailure({
                error: error?.error?.message ?? 'Failed to load department analytics'
              })
            )
          )
        )
      )
    )
  );

  loadSalaryDistribution$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadSalaryDistribution),
      switchMap(({ query }) =>
        this.analyticsService
          .getSalaryDistribution(query)
          .pipe(
            map((data: SalaryDistribution[]) => loadSalaryDistributionSuccess({ data })),
            catchError(error =>
              of(
                loadSalaryDistributionFailure({
                  error: error?.error?.message ?? 'Failed to load salary distribution'
                })
              )
            )
          )
      )
    )
  );

  constructor(
    private readonly actions$: Actions,
    private readonly analyticsService: AnalyticsService
  ) {}
}
