import _ from 'lodash';
import { AsyncStorage } from 'react-native';
import parseJwt from './encodeUtils';

export const CHW_READ_AUTHORITY = 'ROLE_CHW_READ';
export const CHW_WRITE_AUTHORITY = 'ROLE_CHW_WRITE';
export const INCHARGE_READ_AUTHORITY = 'ROLE_INCHARGE_READ';
export const INCHARGE_WRITE_AUTHORITY = 'ROLE_INCHARGE_WRITE';
export const MANAGE_MODULES_AUTHORITY = 'ROLE_MANAGE_MODULES';
export const MANAGE_FACILITIES_AUTHORITY = 'ROLE_MANAGE_FACILITIES';
export const ASSIGN_MODULES_AUTHORITY = 'ROLE_ASSIGN_MODULES';
export const MANAGE_USERS_AUTHORITY = 'ROLE_MANAGE_USERS';
export const DISPLAY_REPORTS_AUTHORITY = 'ROLE_DISPLAY_REPORTS';

export async function hasAuthority(...authorities) {
  const token = await AsyncStorage.getItem('token');
  const tokenDecoded = parseJwt(token);
  const currentUserAuthorities = tokenDecoded.authorities;
  return _.some(currentUserAuthorities, el => _.includes(authorities, el));
}
