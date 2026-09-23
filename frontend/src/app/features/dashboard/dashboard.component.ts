import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { selectUser } from '../../store/auth/auth.selectors';
import { logout } from '../../store/auth/auth.actions';
import { UserInfo } from '../../core/models/auth';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  user$: Observable<UserInfo | null>;

  constructor(
    private readonly store: Store
  ) {
    this.user$ = this.store.select(selectUser);
  }

  logout(): void {
    this.store.dispatch(logout());
  }
}