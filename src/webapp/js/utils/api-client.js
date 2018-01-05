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

const handleError = (error) => {
  const refreshToken = localStorage.getItem('refresh_token');

  switch (error.response.status) {
    case 401:
      if (refreshToken) {
        return dispatch(useRefreshToken(refreshToken, () => apiClient.request(error.config)));
      }

      dispatch(signoutUser());
      break;
    default: {
      const errorMessage = error.response.data.message;
      if (errorMessage) {
        const alertTimeout = Math.max(5000, errorMessage.length * 100);
        Alert.error(errorMessage, {
          timeout: alertTimeout,
        });
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
