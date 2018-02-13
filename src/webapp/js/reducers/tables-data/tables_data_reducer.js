import { FETCH_CHWS, FETCH_INCHARGES, FETCH_USERS } from '../../actions/types';
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
    default:
      return state;
  }
}
