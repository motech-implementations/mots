import { SET_CONNECTION_STATE } from '../actions/types';

const initialState = {
  isConnected: false,
};

export default function (state = initialState, action) {
  switch (action.type) {
    case SET_CONNECTION_STATE: {
      const { type } = action.payload;
      const isConnected = (type !== 'none' && type !== 'unknown');
      return {
        ...state,
        isConnected,
      };
    }
    default:
      return state;
  }
}
