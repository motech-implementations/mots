import base64 from 'base-64';

const CLIENT_URL = 'http://10.0.2.2:8080/api/oauth/token';

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
