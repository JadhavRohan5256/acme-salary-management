import { Country } from "../../core/models/country";
import { Currency } from "../../core/models/currency";

export interface ReferenceDataState {
  countries: Country[];
  currencies: Currency[];

  countriesLoading: boolean;
  currenciesLoading: boolean;

  countriesError: string | null;
  currenciesError: string | null;
}

export const initialReferenceDataState: ReferenceDataState = {
  countries: [],
  currencies: [],

  countriesLoading: false,
  currenciesLoading: false,

  countriesError: null,
  currenciesError: null
};
