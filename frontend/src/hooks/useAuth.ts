import { useSelector } from 'react-redux';
import type { RootState } from '../store/store';
import { User } from '../types';

export const useAuth = () => {
  const { isAuthenticated, user, loading, error } = useSelector(
    (state: RootState) => state.auth
  );

  return {
    isAuthenticated,
    user: user as User | null,
    loading,
    error,
  };
};

export const useUser = () => {
  const { user } = useAuth();
  return user;
};
