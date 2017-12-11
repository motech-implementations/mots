import jwtDecode from 'jwt-decode';

import { AUTH_USER, UNAUTH_USER } from '../../actions/types';
import { dispatch } from '../../index';

export default () => {
  const token = localStorage.getItem('token');
  if (token) {
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000;

    if (decoded.exp < currentTime) {
      dispatch({ type: UNAUTH_USER });
    } else {
      dispatch({ type: AUTH_USER });
    }
  } else {
    dispatch({ type: UNAUTH_USER });
  }
};
