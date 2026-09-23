import { Employee } from "../../core/models/employee";
import { PageResponse } from "../../core/models/page";
import { SalaryHistory } from "../../core/models/salary-history";

export interface EmployeeState {
  employees: PageResponse<Employee> | null;
  selectedEmployee: Employee | null;
  salaryHistory: SalaryHistory[];
  loading: boolean;
  selectedEmployeeLoading: boolean;
  salaryHistoryLoading: boolean;
  updatingSalary: boolean;
  error: string | null;
  selectedEmployeeError: string | null;
  salaryHistoryError: string | null;
  updateSalaryError: string | null;
  updateSalarySuccess: boolean;
}

export const initialEmployeeState: EmployeeState = {
  employees: null,
  selectedEmployee: null,
  salaryHistory: [],
  loading: false,
  selectedEmployeeLoading: false,
  salaryHistoryLoading: false,
  updatingSalary: false,
  error: null,
  selectedEmployeeError: null,
  salaryHistoryError: null,
  updateSalaryError: null,
  updateSalarySuccess: false
};