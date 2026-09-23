import { createReducer, on } from '@ngrx/store';
import {
    loadCountries,
    loadCountriesSuccess,
    loadCountriesFailure,
    loadCurrencies,
    loadCurrenciesSuccess,
    loadCurrenciesFailure
} from './reference-data.actions';
import { initialReferenceDataState } from './reference-data.state';


export const referenceDataReducer = createReducer(
  initialReferenceDataState,

  on(loadCountries, state => ({
    ...state,
    countriesLoading: true,
    countriesError: null
  })),

  on(loadCountriesSuccess, (state, { countries }) => ({
    ...state,
    countries,
    countriesLoading: false,
    countriesError: null
  })),

  on(loadCountriesFailure, (state, { error }) => ({
    ...state,
    countriesLoading: false,
    countriesError: error
  })),

  on(loadCurrencies, state => ({
    ...state,
    currenciesLoading: true,
    currenciesError: null
  })),

  on(loadCurrenciesSuccess, (state, { currencies }) => ({
    ...state,
    currencies,
    currenciesLoading: false,
    currenciesError: null
  })),

  on(loadCurrenciesFailure, (state, { error }) => ({
    ...state,
    currenciesLoading: false,
    currenciesError: error
  }))
);
