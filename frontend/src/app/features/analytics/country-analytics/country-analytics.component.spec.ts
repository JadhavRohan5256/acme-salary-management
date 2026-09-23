import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { provideMockStore, MockStore } from '@ngrx/store/testing';

import { CountryAnalyticsComponent } from './country-analytics.component';
import { selectByCountry, selectCountryLoading, selectCountryError } from '../../../store/analytics/analytics.selectors';

describe('CountryAnalyticsComponent', () => {
  let component: CountryAnalyticsComponent;
  let fixture: ComponentFixture<CountryAnalyticsComponent>;
  let store: MockStore;

  const initialState = {
    analytics: {
      byCountry: [],
      countryLoading: false,
      countryError: null
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CountryAnalyticsComponent ],
      providers: [
        provideMockStore({
          initialState
        })
      ],
      schemas: [ NO_ERRORS_SCHEMA ]
    }).compileComponents();

    store = TestBed.inject(MockStore);

    fixture = TestBed.createComponent(CountryAnalyticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    store.resetSelectors();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should expose country analytics observable', (done) => {
    const data = [
      {
        country: {
          id: 1,
          name: 'India',
          code: 'IN'
        },
        employeeCount: 1250,
        currency: {
          id: 1,
          code: 'INR',
          name: 'Indian Rupee',
          symbol: '₹'
        },
        averageSalary: 85000,
        minimumSalary: 30000,
        maximumSalary: 150000
      }
    ];

    store.overrideSelector(selectByCountry, data);
    store.refreshState();

    component.countryAnalytics$.subscribe(value => {
      expect(value).toEqual(data);
      done();
    });
  });

  it('should expose loading state', (done) => {
    store.overrideSelector(selectCountryLoading, true);
    store.refreshState();

    component.loading$.subscribe(value => {
      expect(value).toBeTrue();
      done();
    });
  });

  it('should expose error state', (done) => {
    const error = 'Failed to load country analytics';

    store.overrideSelector(selectCountryError, error);
    store.refreshState();

    component.error$.subscribe(value => {
      expect(value).toBe(error);
      done();
    });
  });

  it('should have correct displayed columns', () => {
    expect(component.displayedColumns).toEqual([
      'country',
      'employeeCount',
      'currency',
      'averageSalary',
      'minimumSalary',
      'maximumSalary'
    ]);
  });
});