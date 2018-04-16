import _ from 'lodash';
import axios from 'axios';
import Alert from 'react-s-alert';

import { signoutUser, useRefreshToken } from '../actions';
import { dispatch } from '../index';

const handleSuccess = response => response;

const setTokenHeader = (config) => {
  const token = localStorage.getItem('token');
  if (token) {
    // eslint-disable-next-line no-param-reassign
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
};

const justRejectRequestError = error => Promise.reject(error);

const apiClient = axios.create({});

const getErrorMessage = (errorResponse) => {
  if (errorResponse.status === 400) {
    const error = errorResponse.data;

    // eslint-disable-next-line no-prototype-builtins
    if (error.hasOwnProperty('message') && error.hasOwnProperty('stacktrace')) {
      return error.message;
    }

    return _.values(errorResponse.data).join('\n');
  }

  return errorResponse.data.message;
};

const handleError = (error) => {
  const refreshToken = localStorage.getItem('refresh_token');

  switch (error.response.status) {
    case 401:
      if (refreshToken) {
        return dispatch(useRefreshToken(refreshToken, () => apiClient.request(error.config)));
      }

      dispatch(signoutUser());
      break;
    case 403:
      Alert.error('Access denied.');
      break;
    default: {
      const errorMessage = getErrorMessage(error.response);
      if (errorMessage) {
        Alert.error(errorMessage);
      } else {
        Alert.error(error);
      }
    }
  }
  return Promise.reject(error);
};

apiClient.interceptors.response.use(handleSuccess, handleError);
apiClient.interceptors.request.use(setTokenHeader, justRejectRequestError);

export default apiClient;
