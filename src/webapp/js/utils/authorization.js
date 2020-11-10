import _ from 'lodash';
import jwtDecode from 'jwt-decode';

export const CHW_READ_AUTHORITY = 'ROLE_CHW_READ';
export const CHW_WRITE_AUTHORITY = 'ROLE_CHW_WRITE';
export const MANAGE_MODULES_AUTHORITY = 'ROLE_MANAGE_MODULES';
export const DISPLAY_MODULES_AUTHORITY = 'ROLE_DISPLAY_MODULES';
export const MANAGE_FACILITIES_AUTHORITY = 'ROLE_MANAGE_FACILITIES';
export const CREATE_FACILITIES_AUTHORITY = 'ROLE_CREATE_FACILITIES';
export const MANAGE_OWN_FACILITIES_AUTHORITY = 'ROLE_MANAGE_OWN_FACILITIES';
export const DISPLAY_FACILITIES_AUTHORITY = 'ROLE_DISPLAY_FACILITIES';
export const ASSIGN_MODULES_AUTHORITY = 'ROLE_ASSIGN_MODULES';
export const MANAGE_USERS_AUTHORITY = 'ROLE_MANAGE_USERS';
export const DISPLAY_REPORTS_AUTHORITY = 'ROLE_DISPLAY_REPORTS';
export const UPLOAD_CHW_CSV_AUTHORITY = 'ROLE_UPLOAD_CHW_CSV';
export const UPLOAD_LOCATION_CSV_AUTHORITY = 'ROLE_UPLOAD_LOCATION_CSV';
export const GROUP_READ_AUTHORITY = 'ROLE_GROUP_READ';
export const GROUP_WRITE_AUTHORITY = 'ROLE_GROUP_WRITE';
export const AUTOMATED_REPORT_AUTHORITY = 'AUTOMATED_REPORT_AUTHORITY';

export function hasAuthority(...authorities) {
  const token = localStorage.getItem('token');
  const tokenDecoded = jwtDecode(token);
  const currentUserAuthorities = tokenDecoded.authorities;
  return _.some(currentUserAuthorities, el => _.includes(authorities, el));
}

export function canEditLocation(location) {
  if (hasAuthority(MANAGE_FACILITIES_AUTHORITY)) {
    return true;
  }
  if (hasAuthority(MANAGE_OWN_FACILITIES_AUTHORITY) && location) {
    const token = localStorage.getItem('token');
    const tokenDecoded = jwtDecode(token);
    const currentUser = tokenDecoded.user_name;
    return currentUser === location.ownerUsername;
  }

  return false;
}
