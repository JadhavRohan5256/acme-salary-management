import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';
import { provideMockStore, MockStore } from '@ngrx/store/testing';
import { By } from '@angular/platform-browser';

import { AnalyticsComponent } from './analytics.component';
import { selectOverview, selectOverviewError, selectOverviewLoading } from '../../store/analytics/analytics.selectors';
import { loadByCountry, loadByDepartment, loadOverview } from '../../store/analytics/analytics.actions';


describe('AnalyticsComponent', () => {
  let component: AnalyticsComponent;
  let fixture: ComponentFixture<AnalyticsComponent>;
  let store: MockStore;
  let router: jasmine.SpyObj<Router>;

  const initialState = {
    analytics: {
      overview: null,
      byCountry: [],
      byDepartment: [],
      salaryDistribution: [],
      selectedSalaryDistributionQuery: null,

      overviewLoading: false,
      countryLoading: false,
      departmentLoading: false,
      salaryDistributionLoading: false,

      overviewError: null,
      countryError: null,
      departmentError: null,
      salaryDistributionError: null
    }
  };

  beforeEach(async () => {
    router = jasmine.createSpyObj<Router>('Router', [
      'navigate'
    ]);

    await TestBed.configureTestingModule({
      declarations: [ AnalyticsComponent ],
      providers: [
        provideMockStore({
          initialState
        }),
        {
          provide: Router,
          useValue: router
        }
      ],
      schemas: [ NO_ERRORS_SCHEMA ]
    }).compileComponents();

    store = TestBed.inject(MockStore);

    fixture = TestBed.createComponent(AnalyticsComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  afterEach(() => {
    store.resetSelectors();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should expose overview observable', (done) => {
    const overview = {
      totalEmployees: 10000,
      totalCountries: 8,
      totalDepartments: 8
    };

    store.overrideSelector(selectOverview, overview);
    store.refreshState();

    component.overview$.subscribe(value => {
      expect(value).toEqual(overview);
      done();
    });
  });

  it('should expose overview loading state', (done) => {
    store.overrideSelector(selectOverviewLoading, true);
    store.refreshState();

    component.overviewLoading$.subscribe(value => {
      expect(value).toBeTrue();
      done();
    });
  });

  it('should expose overview error', (done) => {
    const error = 'Failed to load analytics overview';

    store.overrideSelector(selectOverviewError, error);
    store.refreshState();

    component.overviewError$.subscribe(value => {
      expect(value).toBe(error);
      done();
    });
  });

  it('should dispatch analytics actions on initialization', () => {
    const dispatchSpy = spyOn(store, 'dispatch');

    component.ngOnInit();

    expect(dispatchSpy).toHaveBeenCalledWith(loadOverview());
    expect(dispatchSpy).toHaveBeenCalledWith(loadByCountry());
    expect(dispatchSpy).toHaveBeenCalledWith(loadByDepartment());
    expect(dispatchSpy).toHaveBeenCalledTimes(3);
  });

  it('should dispatch analytics actions when refresh is called', () => {
    const dispatchSpy = spyOn(store, 'dispatch');

    component.refresh();

    expect(dispatchSpy).toHaveBeenCalledWith(loadOverview());
    expect(dispatchSpy).toHaveBeenCalledWith(loadByCountry());
    expect(dispatchSpy).toHaveBeenCalledWith(loadByDepartment());
    expect(dispatchSpy).toHaveBeenCalledTimes(3);
  });

  it('should navigate back to dashboard', () => {
    component.goBack();

    expect(router.navigate).toHaveBeenCalledWith([
      '/dashboard'
    ]);
  });

  it('should call loadAnalytics from refresh', () => {
    const loadAnalyticsSpy = spyOn(component, 'loadAnalytics');

    component.refresh();

    expect(loadAnalyticsSpy).toHaveBeenCalled();
  });
});