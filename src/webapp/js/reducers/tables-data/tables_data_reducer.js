import { FETCH_CHWS, FETCH_INCHARGES } from '../../actions/types';
import initialTablesData from './tables_data_initial_state';

export default function (state = initialTablesData, action) {
  const newState = {
    ...state,
  };
  switch (action.type) {
    case FETCH_CHWS:
      if (action.payload.data !== undefined) {
        newState.chwList = action.payload.data;
      }
      return newState;

    case FETCH_INCHARGES:
      if (action.payload.data !== undefined) {
        newState.inchargesList = action.payload.data;
      }
      return newState;
    default:
      return newState;
  }
}
