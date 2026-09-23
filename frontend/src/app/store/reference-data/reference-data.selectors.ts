import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ReferenceDataState } from './reference-data.state';


export const selectReferenceDataState = createFeatureSelector<ReferenceDataState>('referenceData');

export const selectCountries = createSelector(
  selectReferenceDataState,
  state => state.countries
);

export const selectCurrencies = createSelector(
  selectReferenceDataState,
  state => state.currencies
);

export const selectCountriesLoading = createSelector(
  selectReferenceDataState,
  state => state.countriesLoading
);

export const selectCurrenciesLoading = createSelector(
  selectReferenceDataState,
  state => state.currenciesLoading
);

export const selectCountriesError = createSelector(
  selectReferenceDataState,
  state => state.countriesError
);

export const selectCurrenciesError = createSelector(
  selectReferenceDataState,
  state => state.currenciesError
);
