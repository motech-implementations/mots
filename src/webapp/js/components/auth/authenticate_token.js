import jwtDecode from 'jwt-decode';

import { AUTH_USER, UNAUTH_USER, SET_COUNTER_LOGOUT_TIME } from '../../actions/types';
import { useRefreshToken } from '../../actions';
import { dispatch } from '../../index';

export default () => {
  const token = localStorage.getItem('token');
  if (token) {
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000;
    if (decoded.exp < currentTime) {
      const refreshToken = localStorage.getItem('refresh_token');

      if (refreshToken) {
        const refreshDecoded = jwtDecode(refreshToken);

        if (refreshDecoded.exp < currentTime) {
          dispatch({ type: UNAUTH_USER });
        } else {
          dispatch(useRefreshToken(refreshToken));
        }
      } else {
        dispatch({ type: UNAUTH_USER });
      }
    } else {
      dispatch({ type: AUTH_USER });
      dispatch({
        type: SET_COUNTER_LOGOUT_TIME,
        payload: decoded.exp_period,
      });
    }
  } else {
    dispatch({ type: UNAUTH_USER });
  }
};
