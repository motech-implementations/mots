import { FETCH_CHWS, FETCH_INCHARGES, CREATE_HEALTH_WORKER_REQUEST,
  CREATE_HEALTH_WORKER_SUCCESS } from '../../actions/types';
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
    case CREATE_HEALTH_WORKER_REQUEST: {
      const { newHealthWorker } = action.payload;
      newHealthWorker.needSynchronize = true;
      return {
        ...state,
        chwList: [...state.chwList, newHealthWorker],
      };
    }
    case CREATE_HEALTH_WORKER_SUCCESS: {
      const { id } = action.meta;
      return {
        ...state,
        chwList: state.chwList.map(chw => (
          (chw.chwId === id) ? { ...chw, needSynchronize: false } : chw)),
      };
    }
    default:
      return state;
  }
}
