import base64 from 'base-64';

import Config from '../config';

const CLIENT_URL = `${Config.api[Config.backend.instance]}/api/oauth/token`;

export default class AuthClient {
  constructor({ clientId, clientSecret }) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  getToken(username, password) {
    return fetch(CLIENT_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Authorization: `Basic ${this.getAuthorizationKey()}`,
      },
      body: `grant_type=password&username=${username}&password=${password}`,
    });
  }

  getAuthorizationKey() {
    return base64.encode(`${this.clientId}:${this.clientSecret}`);
  }
}
