import { FETCH_CHWS, FETCH_INCHARGES } from '../../actions/types';
import initialTablesData from './tables_data_initial_state';

export default function (state = initialTablesData, action) {
  switch (action.type) {
    case FETCH_CHWS:
      if (action.payload !== undefined) {
        return {
          ...state,
          chwList: action.payload,
        };
      }
      return state;

    case FETCH_INCHARGES:
      if (action.payload !== undefined) {
        return {
          ...state,
          inchargesList: action.payload,
        };
      }
      return state;
    default:
      return state;
  }
}
