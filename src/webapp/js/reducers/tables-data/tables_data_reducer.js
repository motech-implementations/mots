import {
  FETCH_SECTORS,
  FETCH_CHWS, FETCH_COMMUNITIES, FETCH_DISTRICTS, FETCH_FACILITIES,
  FETCH_ROLES, SEARCH_ROLES, FETCH_PERMISSIONS,
  FETCH_USERS,
} from '../../actions/types';
import initialTablesData from './tables_data_initial_state';

export default function (state = initialTablesData, action) {
  switch (action.type) {
    case FETCH_CHWS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          chwList: action.payload.data.content,
          chwListPages: action.payload.data.totalPages,
        };
      }
      return state;
    case FETCH_USERS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          usersList: action.payload.data.content,
          userListPages: action.payload.data.totalPages,
        };
      }
      return state;
    case FETCH_COMMUNITIES:
    case FETCH_FACILITIES:
    case FETCH_SECTORS:
    case FETCH_DISTRICTS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          locationsList: action.payload.data.content,
          locationListPages: action.payload.data.totalPages,
        };
      }
      return state;
    case FETCH_ROLES:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          roles: action.payload.data,
        };
      }
      return state;
    case SEARCH_ROLES:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          filteredRoles: action.payload.data.content,
          rolesTotalPages: action.payload.data.totalPages,
        };
      }
      return state;
    case FETCH_PERMISSIONS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          permissions: action.payload.data,
        };
      }
      return state;
    default:
      return state;
  }
}
