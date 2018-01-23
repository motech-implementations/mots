import _ from 'lodash';
import { AsyncStorage } from 'react-native';

import Config from '../config';

const CLIENT_URL = Config.api[Config.backend.instance];
const VALID_STATUSES = [200, 201];

export default class ApiClient {
  static async handleError(obj) {
    console.log(obj);
  }

  static async chooseMethod(method, route, body) {
    return ApiClient.motsFetch({
      method,
      url: route,
      body: body || {}
    })
      .then((response) => {
        const json = response.json();
        if (VALID_STATUSES.indexOf(response.status) !== -1) {
          return json;
        }
        return json.then((obj) => {
          throw obj;
        })
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
