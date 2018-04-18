import { FETCH_CHWS, FETCH_INCHARGES, CREATE_HEALTH_WORKER_REQUEST,
  CREATE_HEALTH_WORKER_SUCCESS, FETCH_USERS, FETCH_ERROR, FETCH_ROLES } from '../../actions/types';
import initialTablesData from './tables_data_initial_state';

export default function (state = initialTablesData, action) {
  switch (action.type) {
    case FETCH_CHWS:
      if (action.payload && action.payload.content) {
        return {
          ...state,
          chwList: action.payload.content || [],
        };
      }
      return state;
    case FETCH_INCHARGES:
      if (action.payload && action.payload.content) {
        return {
          ...state,
          inchargesList: action.payload.content || [],
        };
      }
      return state;
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
    case FETCH_USERS:
      if (action.payload && action.payload.content) {
        return {
          ...state,
          userList: action.payload.content.map(user => ({
            ...user,
            role: user.roles ? user.roles[0].name : '',
          })) || [],
        };
      }
      return state;
    case FETCH_ROLES:
      return {
        ...state,
        roles: action.payload || [],
      };
    case FETCH_ERROR:
      return {
        ...state,
        fetchError: action.payload || false,
      };
    default:
      return state;
  }
}
