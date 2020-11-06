import {
  FETCH_AUTOMATED_REPORTS,
} from '../actions/types';

const initialState = {
  automatedReports: [],
};

export default (state = initialState, action) => {
  switch (action.type) {
    case FETCH_AUTOMATED_REPORTS:
      return { ...state, automatedReports: action.payload };
    default:
      return state;
  }
};
