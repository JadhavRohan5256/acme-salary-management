import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { CoreModule } from './core/core.module';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { authReducer } from './store/auth/auth.reducer';
import { AuthEffects } from './store/auth/auth.effects';
import { employeeReducer } from './store/employees/employee.reducer';
import { EmployeeEffects } from './store/employees/employee.effects';
import { referenceDataReducer } from './store/reference-data/reference-data.reducer';
import { ReferenceDataEffects } from './store/reference-data/reference-data.effects';
import { analyticsReducer } from './store/analytics/analytics.reducer';
import { AnalyticsEffects } from './store/analytics/analytics.effects';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    CoreModule,
    StoreModule.forRoot({
      auth: authReducer,
      employees: employeeReducer,
      referenceData: referenceDataReducer,
      analytics: analyticsReducer
    }),
    EffectsModule.forRoot([
      AuthEffects,
      EmployeeEffects,
      ReferenceDataEffects,
      AnalyticsEffects
    ])
  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
