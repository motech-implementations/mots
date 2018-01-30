import _ from 'lodash';
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
      const newChwList = _.clone(state.chwList);
      const { newHealthWorker } = action.payload;
      newHealthWorker.needSynchronize = true;
      newChwList.push(newHealthWorker);
      return {
        ...state,
        chwList: newChwList,
      };
    }
    case CREATE_HEALTH_WORKER_SUCCESS: {
      const { id } = action.meta;
      const newChwList = state.chwList.map((chw) => {
        const newChw = chw;
        if (chw.chwId === id) {
          newChw.needSynchronize = false;
        }
        return chw;
      });
      return {
        ...state,
        chwList: newChwList,
      };
    }
    default:
      return state;
  }
}
