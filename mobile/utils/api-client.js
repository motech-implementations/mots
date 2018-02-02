import _ from 'lodash';
import { AsyncStorage, Alert } from 'react-native';

import Config from '../config';
import { signoutUser, useRefreshToken } from '../actions';
import { dispatch } from '../App';

const CLIENT_URL = Config.api[Config.backend.instance];
const VALID_STATUSES = [200, 201];

const getErrorMessage = (errorResponse) => {
  if (errorResponse.status === 400) {
    return _.values(errorResponse.data).join('\n');
  }

  return errorResponse.data.message;
};

const getAlert = (title, message) => Alert.alert(
  title,
  message,
  [{ text: 'OK' }],
  { cancelable: false },
);

export default class ApiClient {
  static async handleError(error) {
    const refreshToken = await AsyncStorage.getItem('refresh_token');

    switch (error.status) {
      case 401:
        if (refreshToken) {
          return dispatch(useRefreshToken(refreshToken));
        }
        return dispatch(signoutUser());
      case 403:
        getAlert('Access denied.', 'You don\'t have permissions to fetch this data');
        break;
      case 404:
        getAlert('Resource not found.', 'There were some problems with data fetching.');
        break;
      case 500:
        getAlert('Error occurred.', 'There was an internal server error.');
        break;
      default: {
        const errorMessage = getErrorMessage(error.response);
        const message = errorMessage || error;
        getAlert('Error occurred.', message);
      }
    }
    return error;
  }

  static async chooseMethod(method, route, body) {
    return ApiClient.motsFetch({
      method,
      url: route,
      body: body || {},
    })
      .then((response) => {
        if (VALID_STATUSES.indexOf(response.status) !== -1) {
          return response.json();
        }
        throw response;
      })
      .catch((obj) => {
        ApiClient.handleError(obj);
      });
  }

  static async get(route) {
    return ApiClient.chooseMethod('GET', route);
  }

  static async post(route, body) {
    return ApiClient.chooseMethod('POST', route, body);
  }

  static async put(route, body) {
    return ApiClient.chooseMethod('PUT', route, body);
  }

  static async delete(route) {
    return ApiClient.chooseMethod('DELETE', route);
  }

  static async motsFetch(_options) {
    const options = _.extend({
      method: 'GET',
      url: null,
      body: null,
    }, _options);

    const fetchOptions = {
      method: options.method,
      headers: {},
    };

    const token = await AsyncStorage.getItem('token');
    fetchOptions.headers.Authorization = `Bearer ${token}`;

    if (options.method in ['POST', 'PUT', 'DELETE']) {
      fetchOptions.headers.Accept = 'application/json';
      fetchOptions.headers['Content-Type'] = 'application/json';
    }

    return fetch(CLIENT_URL + options.url, fetchOptions);
  }
}
