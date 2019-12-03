import _ from 'lodash';
import { AsyncStorage } from 'react-native';
import bcrypt from 'react-native-bcrypt';
import { generateSecureRandom } from 'react-native-securerandom';
import parseJwt from './encodeUtils';

// use a custom PRNG as Math.random is not cryptographically secure
bcrypt.setRandomFallback(len => generateSecureRandom(len));
// Cost factor determines number of iterations (2^8 = 256).
// Use the highest possible number that doesn't affect performance.
const BCRYPT_COST_FACTOR = 8;

export const CHW_READ_AUTHORITY = 'ROLE_CHW_READ';
export const CHW_WRITE_AUTHORITY = 'ROLE_CHW_WRITE';
export const MANAGE_MODULES_AUTHORITY = 'ROLE_MANAGE_MODULES';
export const MANAGE_FACILITIES_AUTHORITY = 'ROLE_MANAGE_FACILITIES';
export const ASSIGN_MODULES_AUTHORITY = 'ROLE_ASSIGN_MODULES';
export const MANAGE_USERS_AUTHORITY = 'ROLE_MANAGE_USERS';
export const DISPLAY_REPORTS_AUTHORITY = 'ROLE_DISPLAY_REPORTS';
export const DISPLAY_MODULES_AUTHORITY = 'ROLE_DISPLAY_MODULES';

export async function hasAuthority(...authorities) {
  const token = await AsyncStorage.getItem('token');
  const tokenDecoded = parseJwt(token);
  const currentUserAuthorities = tokenDecoded.authorities;
  return _.some(currentUserAuthorities, el => _.includes(authorities, el));
}

export function getHash(password, callback) {
  bcrypt.genSalt(BCRYPT_COST_FACTOR, (saltErr, salt) =>
    bcrypt.hash(password, salt, (hashErr, hash) => callback(hash)));
}

export function checkHash(string, hash, callback) {
  bcrypt.compare(string, hash, (err, res) => callback(res));
}
