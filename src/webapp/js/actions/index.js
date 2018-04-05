/* eslint-disable no-param-reassign */
import axios from 'axios';
import Client from 'client-oauth2';
import jwtDecode from 'jwt-decode';
import apiClient from '../utils/api-client';

import {
  AUTH_USER, UNAUTH_USER, AUTH_ERROR, FETCH_CHWS,
  SAVE_HEALTH_WORKER, FETCH_LOCATIONS, CREATE_INCHARGE, FETCH_INCHARGES,
  SAVE_INCHARGE,
  SET_COUNTER_LOGOUT_TIME, RESET_LOGOUT_COUNTER, FETCH_USERS, FETCH_CHIEFDOMS,
  FETCH_DISTRICTS, FETCH_FACILITIES, FETCH_COMMUNITIES, CREATE_USER,
  FETCH_ROLES, SAVE_USER, CREATE_FACILITY, CREATE_COMMUNITY, SAVE_COMMUNITY,
  SAVE_FACILITY, SAVE_USER_PROFILE, FETCH_REPORTS,
} from './types';

const BASE_URL = '/api';
const AUTH_URL = `${BASE_URL}/oauth/token`;

const CLIENT_ID = 'trusted-client';
const CLIENT_SECRET = 'secret';

const authClient = new Client({
  clientId: CLIENT_ID,
  clientSecret: CLIENT_SECRET,
  accessTokenUri: AUTH_URL,
});

export function authError(error) {
  return {
    type: AUTH_ERROR,
    payload: error,
  };
}

export function signinUser({ username, password }, callback) {
  return (dispatch) => {
    authClient.owner.getToken(username, password)
      .then((response) => {
        dispatch({ type: AUTH_USER });
        const tokenDecoded = jwtDecode(response.accessToken);
        dispatch({
          type: SET_COUNTER_LOGOUT_TIME,
          payload: tokenDecoded.exp_period,
        });
        localStorage.setItem('token', response.accessToken);
        localStorage.setItem('refresh_token', response.refreshToken);
        callback();
      })
      .catch(() => {
        dispatch(authError('Wrong username or password. Please try again.'));
      });
  };
}

export function useRefreshToken(refreshToken, callback) {
  return dispatch => axios({
    method: 'post',
    url: AUTH_URL,
    auth: {
      username: CLIENT_ID,
      password: CLIENT_SECRET,
    },
    params: {
      grant_type: 'refresh_token',
      refresh_token: refreshToken,
    },
  })
    .catch(() => {
      dispatch(authError('Error occurred when refreshing the user session'));
    })
    .then((response) => {
      localStorage.setItem('token', response.data.access_token);
      localStorage.setItem('refresh_token', response.data.refresh_token);
      dispatch({ type: AUTH_USER });

      if (callback) {
        return callback();
      }

      return null;
    });
}

export function signoutUser() {
  return { type: UNAUTH_USER };
}

export function resetLogoutCounter() {
  return (dispatch) => {
    const token = localStorage.getItem('token');
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000;
    if (decoded.exp < currentTime) {
      const refreshToken = localStorage.getItem('refresh_token');
      dispatch(useRefreshToken(refreshToken));
    }

    dispatch({ type: RESET_LOGOUT_COUNTER });
  };
}

export function fetchChws(searchParams, selected) {
  const url = `${BASE_URL}/chw/search`;

  searchParams.selected = selected;

  const request = apiClient.get(url, {
    params: searchParams,
  });

  return {
    type: FETCH_CHWS,
    payload: request,
  };
}

export function fetchIncharges(searchParams, selected) {
  const url = `${BASE_URL}/incharge/search`;

  searchParams.selected = selected;

  const request = apiClient.get(url, {
    params: searchParams,
  });

  return {
    type: FETCH_INCHARGES,
    payload: request,
  };
}

export function saveHealthWorker(values, callback) {
  const request = apiClient.put(`${BASE_URL}/chw/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_HEALTH_WORKER,
    payload: request,
  };
}

export function selectHealthWorker(values, callback) {
  const request = apiClient.put(`${BASE_URL}/chw/${values.id}/select`, values);
  request.then(() => callback());

  return {
    type: SAVE_HEALTH_WORKER,
    payload: request,
  };
}

export function selectIncharge(values, callback) {
  const request = apiClient.put(`${BASE_URL}/incharge/${values.id}/select`, values);
  request.then(() => callback());

  return {
    type: CREATE_INCHARGE,
    payload: request,
  };
}

export function saveIncharge(values, callback) {
  const request = apiClient.put(`${BASE_URL}/incharge/${values.id}`, values);
  request.then(() => callback());

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

export function fetchUsers(searchParams) {
  const url = '/api/user/search';
  const request = apiClient.get(url, {
    params: searchParams,
  });

  return {
    type: FETCH_USERS,
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

export function createUser(values, callback) {
  const request = apiClient.post(`${BASE_URL}/user`, values);
  request.then(() => callback());

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

export function createFacility(values, callback) {
  const request = apiClient.post(`${BASE_URL}/facility`, values);
  request.then(() => callback());

  return {
    type: CREATE_FACILITY,
    payload: request,
  };
}

export function saveFacility(values, callback) {
  const request = apiClient.put(`${BASE_URL}/facility/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_FACILITY,
    payload: request,
  };
}

export function createCommunity(values, callback) {
  const request = apiClient.post(`${BASE_URL}/community`, values);
  request.then(() => callback());

  return {
    type: CREATE_COMMUNITY,
    payload: request,
  };
}

export function saveCommunity(values, callback) {
  const request = apiClient.put(`${BASE_URL}/community/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_COMMUNITY,
    payload: request,
  };
}

export function saveUserProfile(values, callback) {
  const request = apiClient.put(`${BASE_URL}/user/profile/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_USER_PROFILE,
    payload: request,
  };
}

export function fetchLocationsOfType(type, searchParams) {
  let url;

  switch (type) {
    case FETCH_DISTRICTS:
      url = `${BASE_URL}/district/locations/search`;
      break;
    case FETCH_CHIEFDOMS:
      url = `${BASE_URL}/chiefdom/locations/search`;
      break;
    case FETCH_FACILITIES:
      url = `${BASE_URL}/facility/locations/search`;
      break;
    case FETCH_COMMUNITIES:
      url = `${BASE_URL}/community/locations/search`;
      break;
    default:
      throw new Error('Unexpected use of fetchLocationsOfType method');
  }
  const request = apiClient.get(url, {
    params: searchParams,
  });

  return {
    type,
    payload: request,
  };
}

export function fetchReports() {
  const url = '/api/reports/templates/';
  const request = apiClient.get(url);

  return {
    type: FETCH_REPORTS,
    payload: request,
  };
}
