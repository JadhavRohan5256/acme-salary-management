import { createReducer, on } from '@ngrx/store';
import { initialAuthState } from './auth.state';
import { login, loginSuccess, loginFailure, logout } from './auth.actions';

export const authReducer = createReducer(
  initialAuthState,

  on(login, state => ({
    ...state,
    loading: true,
    error: null
  })),

  on(loginSuccess, (state, { response }) => ({
    ...state,
    token: response.accessToken,
    user: response.user,
    loading: false,
    error: null
  })),

  on(loginFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(logout, () => initialAuthState)
);