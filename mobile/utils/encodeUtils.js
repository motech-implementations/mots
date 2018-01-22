const base64Decoder = require('base-64');

export default function parseJwt(token) {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace('-', '+').replace('_', '/');
  return JSON.parse(base64Decoder.decode(base64));
}
