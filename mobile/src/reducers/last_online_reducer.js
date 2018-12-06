import { SET_LAST_ONLINE_TIME } from '../actions/types';

const initialState = {
  lastOnlineTime: null,
};

export default function (state = initialState, action) {
  switch (action.type) {
    case SET_LAST_ONLINE_TIME: {
      const isConnected = action.payload;
      return { ...state, lastOnlineTime: isConnected ? null : new Date().getTime() };
    }
    default:
      return state;
  }
}
