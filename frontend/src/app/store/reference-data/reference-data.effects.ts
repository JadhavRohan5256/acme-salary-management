import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, of, switchMap } from 'rxjs';

import {
    loadCountries,
    loadCountriesSuccess,
    loadCountriesFailure,
    loadCurrencies,
    loadCurrenciesSuccess,
    loadCurrenciesFailure
} from './reference-data.actions';
import { Country } from '../../core/models/country';
import { Currency } from '../../core/models/currency';
import { ReferenceDataService } from '../../core/services/reference-data/reference-data.service';

@Injectable()
export class ReferenceDataEffects {

  loadCountries$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadCountries),
      switchMap(() =>
        this.referenceDataService.getCountries().pipe(
          map((countries: Country[]) => loadCountriesSuccess({ countries })),
          catchError(error =>
            of(
              loadCountriesFailure({
                error: error?.error?.message ?? 'Failed to load countries'
              })
            )
          )
        )
      )
    )
  );

  loadCurrencies$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadCurrencies),
      switchMap(() =>
        this.referenceDataService.getCurrencies().pipe(
          map((currencies: Currency[]) => loadCurrenciesSuccess({ currencies })),
          catchError(error =>
            of(
              loadCurrenciesFailure({
                error: error?.error?.message ?? 'Failed to load currencies'
              })
            )
          )
        )
      )
    )
  );

  constructor(
    private readonly actions$: Actions,
    private readonly referenceDataService: ReferenceDataService
  ) {}
}
