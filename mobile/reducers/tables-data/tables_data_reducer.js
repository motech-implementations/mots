import { FETCH_CHWS, FETCH_INCHARGES } from '../../actions/types';
import initialTablesData from './tables_data_initial_state';

export default function (state = initialTablesData, action) {
  switch (action.type) {
    case FETCH_CHWS:
      return {
        ...state,
        chwList: action.payload || [],
      };

    case FETCH_INCHARGES:
      return {
        ...state,
        inchargesList: action.payload || [],
      };

    default:
      return state;
  }
}
