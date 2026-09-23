import { createReducer, on } from '@ngrx/store';
import { AuthState, initialAuthState } from './auth.state';
import { login, loginSuccess, loginFailure, logout } from './auth.actions';


const getInitialAuthState = (): AuthState => {
  const token = localStorage.getItem('acme_access_token');
  const userJson = localStorage.getItem('acme_user');
  let user = null;

  if (userJson) {
    try {
      user = JSON.parse(userJson);
    } catch {
      user = null;
    }
  }

  return {
    token,
    user,
    loading: false,
    error: null
  };
};


export const authReducer = createReducer(
  getInitialAuthState(),

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