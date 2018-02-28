import {
  FETCH_CHIEFDOMS,
  FETCH_CHWS, FETCH_COMMUNITIES, FETCH_DISTRICTS, FETCH_FACILITIES,
  FETCH_INCHARGES, FETCH_ROLES,
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

    case FETCH_INCHARGES:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          inchargesList: action.payload.data.content,
          inchargeListPages: action.payload.data.totalPages,
        };
      }
      return state;
    case FETCH_USERS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          usersList: action.payload.data,
        };
      }
      return state;
    case FETCH_COMMUNITIES:
    case FETCH_FACILITIES:
    case FETCH_CHIEFDOMS:
    case FETCH_DISTRICTS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          locationsList: action.payload.data,
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
    default:
      return state;
  }
}
