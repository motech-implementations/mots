import _ from 'lodash';
import { Actions } from 'react-native-router-flux';
import { AsyncStorage, PermissionsAndroid, Platform } from 'react-native';
import RNFetchBlob from 'rn-fetch-blob';
import jwtDecode from 'jwt-decode';

import Config from '../../config';
import { signoutUser, useRefreshToken } from '../actions';
import { dispatch } from '../App';
import { FETCH_ERROR } from '../actions/types';

const CLIENT_URL = Config.api[Config.backend.instance];
const VALID_STATUSES = [200, 201, 204];

const getErrorMessage = (errorResponse) => {
  if (errorResponse) {
    return _.values(errorResponse).join('\n');
  }

  return '';
};

const getAlert = (title, message) => {
  Actions.modalError({
    message, title,
  });
};

export default class ApiClient {
  static async handleError(error, callback) {
    const refreshToken = await AsyncStorage.getItem('refresh_token');

    switch (error.status) {
      case 401: {
        const refreshDecoded = (refreshToken) ? jwtDecode(refreshToken) : null;
        const currentTime = Date.now() / 1000;
        if (refreshDecoded && refreshDecoded.exp > currentTime) {
          return useRefreshToken(refreshToken, dispatch, callback);
        }
        dispatch(signoutUser());
        break;
      }
      case 403:
        getAlert('Access denied.', 'You don\'t have permissions to fetch this data');
        break;
      case 404:
        getAlert('Resource not found.', 'There were some problems with data fetching.');
        break;
      case 500:
        ApiClient.safeJson(error).then((data) => {
          const errorMessage = data.message ? data.message : getErrorMessage(data);
          const message = errorMessage || data;
          getAlert('Error occurred.', message);
        }).catch(() =>
          getAlert('Error occurred.', 'There was an internal server error.'));
        break;
      default: {
        ApiClient.safeJson(error).then((data) => {
          const errorMessage = getErrorMessage(data);
          const message = errorMessage || data;
          getAlert('Error occurred.', message);
        }).catch(() => getAlert('Error occurred.', 'Unable to get a valid response from the server. Please check your internet connection before continuing.'));
      }
    }
    return null;
  }

  static async safeJson(data) {
    try {
      data.json().then(json => json);
    } catch (err) {
      throw new Error(err);
    }
  }

  static async handleResponse(response, type) {
    if (VALID_STATUSES.indexOf(response.status) !== -1) {
      if (type === 'text') {
        return response.text()
          .then(responseText => responseText)
          .catch(() => {});
      }
      if (response.status === 204) {
        return {
          status: response.status,
        };
      }
      return response.json()
        .then(responseJson => responseJson)
        .catch(() => {});
    }
    throw response;
  }

  static async chooseMethod(method, route, type, body) {
    const fetchOptions = {
      method,
      url: route,
      body: JSON.stringify(body) || undefined,
    };
    return ApiClient.motsFetch(fetchOptions)
      .then(response => this.handleResponse(response, type))
      .catch((obj) => {
        dispatch({
          type: FETCH_ERROR,
          payload: true,
        });
        return ApiClient.handleError(obj, () => ApiClient.motsFetch(fetchOptions)
          .then(response => this.handleResponse(response, type)));
      });
  }

  static async get(route) {
    return ApiClient.chooseMethod('GET', route, 'json');
  }

  static async getText(route) {
    return ApiClient.chooseMethod('GET', route, 'text');
  }

  static async post(route, body) {
    return ApiClient.chooseMethod('POST', route, 'json', body);
  }

  static async put(route, body) {
    return ApiClient.chooseMethod('PUT', route, 'json', body);
  }

  static async delete(route) {
    return ApiClient.chooseMethod('DELETE', route, 'json');
  }

  static async send(effect) {
    return ApiClient.chooseMethod(effect.method, effect.url, 'json', effect.body);
  }

  static async motsFetch(_options) {
    dispatch({
      type: FETCH_ERROR,
      payload: false,
    });
    const fetchOptions = _.extend({
      headers: {},
      method: 'GET',
      url: null,
    }, _options);

    const token = await AsyncStorage.getItem('token');
    fetchOptions.headers.Authorization = `Bearer ${token}`;
    if (['POST', 'PUT', 'DELETE'].indexOf(fetchOptions.method) !== -1) {
      fetchOptions.headers.Accept = 'application/json';
      fetchOptions.headers['Content-Type'] = 'application/json';
    }

    return fetch(CLIENT_URL + fetchOptions.url, fetchOptions);
  }

  static async requestWritePermission() {
    const result = await PermissionsAndroid
      .request(PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE);
    return result === PermissionsAndroid.RESULTS.GRANTED;
  }

  static async downloadReport(url, name, extension, isRetry) {
    const isIos = Platform.OS === 'ios';
    const token = await AsyncStorage.getItem('token');
    const mimeType = extension === 'xls' ? 'application/vnd.ms-excel' : 'application/pdf';
    const hasPermission = isIos || await this.requestWritePermission();
    const fileName = `${name.replace(/ /g, '_')}.${extension}`;
    if (hasPermission) {
      return RNFetchBlob
        .config({
          fileCache: true,
          path: `${isIos ? RNFetchBlob.fs.dirs.DocumentDir : RNFetchBlob.fs.dirs.DownloadDir}/${fileName}`,
        })
        .fetch('GET', CLIENT_URL + url, {
          Authorization: `Bearer ${token}`,
        })
        .then((res) => {
          if (res.respInfo.status === 401 && !isRetry) {
            ApiClient.handleError(res.respInfo, () => this
              .downloadReport(url, name, extension, true));
          } else if (isIos) {
            RNFetchBlob.ios.previewDocument(res.path());
          } else {
            RNFetchBlob.android.actionViewIntent(res.path(), mimeType);
          }
        });
    }
    return null;
  }
}
