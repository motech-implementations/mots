import { FETCH_CHWS, FETCH_INCHARGES } from '../../actions/types';
import initialTablesData from './tables_data_initial_state';

export default function (state = initialTablesData, action) {
  switch (action.type) {
    case FETCH_CHWS:
      if (action.payload.data !== undefined) {
        return state.set('chwList', action.payload.data);
      }
      return state;

    case FETCH_INCHARGES:
      if (action.payload.data !== undefined) {
        return state.set('inchargesList', action.payload.data);
      }
      return state;
    default:
      return state;
  }
}
