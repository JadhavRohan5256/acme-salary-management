import { Currency } from "./currency";

export interface SalaryHistory {
  id: number;
  previousSalary: number;
  newSalary: number;
  currency: Currency;
  effectiveDate: string;
  changedBy: string;
  createdAt: string;
}