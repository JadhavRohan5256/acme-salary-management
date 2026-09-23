import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EmployeeListComponent } from './employee-list/employee-list.component';
import { EmployeeDetailsComponent } from './employee-details/employee-details.component';
import { UpdateSalaryPageComponent } from './update-salary-page/update-salary-page.component';

const routes: Routes = [
  {
    path: '',
    component: EmployeeListComponent
  },
  { 
    path: ':id', 
    component: EmployeeDetailsComponent 
  },
  { 
    path: ':id/update-salary', 
    component: UpdateSalaryPageComponent 
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class EmployeesRoutingModule {}