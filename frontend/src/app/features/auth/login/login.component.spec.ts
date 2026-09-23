import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { provideMockStore, MockStore } from '@ngrx/store/testing';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let store: MockStore;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        LoginComponent
      ],
      imports: [
        ReactiveFormsModule
      ],
      providers: [
        provideMockStore()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    store = TestBed.inject(MockStore);
    spyOn(store, 'dispatch');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have invalid form initially', () => {
    expect(component.loginForm.invalid).toBeTrue();
  });

  it('should make form valid when username and password are provided', () => {
    component.loginForm.setValue({
      username: 'admin',
      password: 'password'
    });

    expect(component.loginForm.valid).toBeTrue();
  });

  it('should not dispatch login when form is invalid', () => {
    component.submit();
    expect(store.dispatch).not.toHaveBeenCalled();
  });

  it('should dispatch login when form is valid', () => {
    component.loginForm.setValue({
      username: 'admin',
      password: 'password'
    });
    component.submit();
    expect(store.dispatch).toHaveBeenCalled();
  });

});