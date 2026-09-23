import { Country } from "./country";
import { Currency } from "./currency";


export interface Employee {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  country: Country;
  department: string;
  designation: string;
  currentSalary: number;
  currency: Currency;
  createdAt?: string;
  updatedAt?: string;
}