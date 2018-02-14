import {
  FETCH_CHIEFDOMS,
  FETCH_CHWS, FETCH_COMMUNITIES, FETCH_DISTRICTS, FETCH_FACILITIES,
  FETCH_INCHARGES,
  FETCH_USERS,
} from '../../actions/types';
import initialTablesData from './tables_data_initial_state';

export default function (state = initialTablesData, action) {
  switch (action.type) {
    case FETCH_CHWS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          chwList: action.payload.data,
        };
      }
      return state;

    case FETCH_INCHARGES:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          inchargesList: action.payload.data,
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
      if (action.payload.data !== undefined) {
        return {
          ...state,
          communitiesList: action.payload.data,
        };
      }
      return state;
    case FETCH_FACILITIES:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          facilitiesList: action.payload.data,
        };
      }
      return state;
    case FETCH_CHIEFDOMS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          chiefdomsList: action.payload.data,
        };
      }
      return state;
    case FETCH_DISTRICTS:
      if (action.payload.data !== undefined) {
        return {
          ...state,
          districtsList: action.payload.data,
        };
      }
      return state;
    default:
      return state;
  }
}
