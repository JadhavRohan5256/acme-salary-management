import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectError, selectLoading } from '../../../store/auth/auth.selectors';
import { login } from '../../../store/auth/auth.actions';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading$!: Observable<boolean>;
  error$!: Observable<string | null>;

  constructor(
    private readonly fb: FormBuilder,
    private readonly store: Store
  ) {}

  ngOnInit(): void {
    this.initializeFormGroup();
    this.loading$ = this.store.select(selectLoading);
    this.error$ = this.store.select(selectError);
  }

  private initializeFormGroup() {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['',[Validators.required, Validators.minLength(6)]]
    });
  }


  submit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const request = this.loginForm.value;
    this.store.dispatch(login({ request }));
  }
}