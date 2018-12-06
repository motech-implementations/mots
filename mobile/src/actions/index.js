import apiClient from '../utils/api-client';
import AuthClient from '../utils/auth-client';
import { getHash, checkHash } from '../utils/authorization';

import {
  AUTH_USER,
  UNAUTH_USER,
  AUTH_ERROR,
  FETCH_CHWS,
  SAVE_HEALTH_WORKER,
  FETCH_LOCATIONS,
  CREATE_INCHARGE,
  FETCH_INCHARGES,
  SAVE_INCHARGE,
  FETCH_USERS,
  CREATE_USER,
  SAVE_USER,
  FETCH_ROLES,
  SAVE_PROFILE,
  FETCH_REPORT,
  FETCH_REPORT_TEMPLATES,
  STORE_LOGIN,
  SET_CONNECTION_STATE,
  SET_LAST_ONLINE_TIME,
} from './types';

const BASE_URL = '/api';

const CLIENT_ID = 'trusted-client';
const CLIENT_SECRET = 'secret';

const authClient = new AuthClient({
  clientId: CLIENT_ID,
  clientSecret: CLIENT_SECRET,
});

export function authError(error) {
  return {
    type: AUTH_ERROR,
    payload: error,
  };
}

export function signInOffline(username, password, savedLogin, callback, errorCallback) {
  return (dispatch) => {
    if (savedLogin) {
      checkHash(password, savedLogin.hash, (result) => {
        if (result) {
          dispatch({
            type: AUTH_USER,
            payload: {
              isOffline: true,
              accessToken: savedLogin.token,
              refreshToken: null,
            },
          });
          callback();
        } else {
          dispatch(authError('Wrong username or password. Please try again.'));
          errorCallback();
        }
      });
    } else {
      dispatch(authError('Please try again in online mode.'));
      errorCallback();
    }
  };
}

function storeLogin(dispatch, username, password, token) {
  getHash(password, (hash) => {
    dispatch({
      type: STORE_LOGIN,
      payload: {
        username,
        hash,
        token,
      },
    });
  });
}

export function signIn(username, password, savedLogin, callback, errorCallback) {
  return (dispatch) => {
    authClient.getToken(username, password)
      .then((response) => {
        response.json().then((data) => {
          dispatch({
            type: AUTH_USER,
            payload: {
              accessToken: data.access_token,
              refreshToken: data.refresh_token,
            },
          });
          storeLogin(dispatch, username, password, data.access_token);
          callback();
        }).catch(() => {
          dispatch(authError('Wrong username or password. Please try again.'));
          errorCallback();
        });
      })
      .catch(() => {
        // could not get response from the server. try signing in offline
        dispatch(signInOffline(username, password, savedLogin, callback, errorCallback));
      });
  };
}

export function useRefreshToken(refreshToken, callback) {
  return (dispatch) => {
    authClient.refreshToken(refreshToken)
      .then(response =>
        response.json())
      .then((data) => {
        dispatch({
          type: AUTH_USER,
          payload: {
            accessToken: data.access_token,
            refreshToken: data.refresh_token,
          },
        });

        if (callback) {
          return callback();
        }

        return null;
      })
      .catch(() => {
        dispatch(authError('Error occurred when refreshing token'));
      });
  };
}

export function signoutUser() {
  return { type: UNAUTH_USER };
}

export function fetchChws(parameters) {
  const url = `${BASE_URL}/chw/search?size=100000${parameters}`;
  const request = apiClient.get(url);

  return {
    type: FETCH_CHWS,
    payload: request,
  };
}

export function fetchIncharges(parameters) {
  const url = `${BASE_URL}/incharge/search?&size=10000${parameters}`;
  const request = apiClient.get(url);
  return {
    type: FETCH_INCHARGES,
    payload: request,
  };
}

export function selectHealthWorker(values, callback) {
  const request = apiClient.put(`${BASE_URL}/chw/${values.id}/select`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_HEALTH_WORKER,
    payload: request,
  };
}

export function saveHealthWorker(values, callback) {
  const request = apiClient.put(`${BASE_URL}/chw/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_HEALTH_WORKER,
    payload: request,
  };
}

export function selectIncharge(values, callback) {
  const request = apiClient.put(`${BASE_URL}/incharge/${values.id}/select`, values);
  request.then(result => callback(result));

  return {
    type: CREATE_INCHARGE,
    payload: request,
  };
}

export function saveIncharge(values, callback) {
  const request = apiClient.put(`${BASE_URL}/incharge/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_INCHARGE,
    payload: request,
  };
}

export function fetchLocations() {
  const url = '/api/districts';
  const request = apiClient.get(url);

  return {
    type: FETCH_LOCATIONS,
    payload: request,
  };
}

export function fetchRoles() {
  const url = '/api/role';
  const request = apiClient.get(url);

  return {
    type: FETCH_ROLES,
    payload: request,
  };
}

export function fetchUsers(parameters) {
  const url = `${BASE_URL}/user/search?size=10000${parameters}`;
  const request = apiClient.get(url);

  return {
    type: FETCH_USERS,
    payload: request,
  };
}

export function createUser(values, callback) {
  const request = apiClient.post(`${BASE_URL}/user`, values);
  request.then(result => callback(result));

  return {
    type: CREATE_USER,
    payload: request,
  };
}

export function saveUser(values, callback) {
  const request = apiClient.put(`${BASE_URL}/user/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_USER,
    payload: request,
  };
}

export function saveProfile(values, callback) {
  const request = apiClient.put(`${BASE_URL}/user/profile/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_PROFILE,
    payload: request,
  };
}

export function fetchReport(templateId, version) {
  const url = `/api/reports/templates/${templateId}/json/versioned?version=${version || 0}`;
  const request = apiClient.get(url);

  return {
    type: FETCH_REPORT,
    payload: request,
    meta: { templateId },
  };
}

export function fetchReportTemplates() {
  const url = '/api/reports/templates/';
  const request = apiClient.get(url);

  return {
    type: FETCH_REPORT_TEMPLATES,
    payload: request,
  };
}

export function setConnectionState(connectionInfo) {
  return {
    type: SET_CONNECTION_STATE,
    payload: connectionInfo,
  };
}

export function setLastOnlineTime(connectionInfo) {
  return {
    type: SET_LAST_ONLINE_TIME,
    payload: connectionInfo,
  };
}
