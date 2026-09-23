import { createAction, props } from '@ngrx/store';
import { Country } from '../../core/models/country';
import { Currency } from '../../core/models/currency';


export const loadCountries = createAction(
  '[Reference Data] Load Countries'
);

export const loadCountriesSuccess = createAction(
  '[Reference Data] Load Countries Success',
  props<{ countries: Country[] }>()
);

export const loadCountriesFailure = createAction(
  '[Reference Data] Load Countries Failure',
  props<{ error: string }>()
);

export const loadCurrencies = createAction(
  '[Reference Data] Load Currencies'
);

export const loadCurrenciesSuccess = createAction(
  '[Reference Data] Load Currencies Success',
  props<{ currencies: Currency[] }>()
);

export const loadCurrenciesFailure = createAction(
  '[Reference Data] Load Currencies Failure',
  props<{ error: string }>()
);
