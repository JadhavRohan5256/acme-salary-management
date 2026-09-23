import { UserInfo } from "../../core/models/auth";

export interface AuthState {
  token: string | null;
  user: UserInfo | null;
  loading: boolean;
  error: string | null;
}

export const initialAuthState: AuthState = {
  token: null,
  user: null,
  loading: false,
  error: null
};