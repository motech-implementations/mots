import RestClient from 'react-native-rest-client';
import { AsyncStorage } from 'react-native';

const CLIENT_URL = 'http://10.0.2.2:8080';

export default class ApiClient extends RestClient {
  constructor() {
    super(CLIENT_URL);
    AsyncStorage.getItem('token').then((token) => {
      this.headers.Authorization = `Bearer ${token}`;
    });
  }

  get(route, query) {
    return this.GET(route, query || '');
  }

  post(route, body) {
    return this.POST(route, body || {});
  }

  put(route, body) {
    return this.PUT(route, body || {});
  }

  delete(route, query) {
    return this.DELETE(route, query || '');
  }
}
