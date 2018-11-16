import { FETCH_REPORT, FETCH_REPORT_TEMPLATES } from '../actions/types';

const initialState = {
  reports: {},
  templates: [],
};

export default function (state = initialState, action) {
  switch (action.type) {
    case FETCH_REPORT:
      if (action.payload && action.payload.length) {
        const newState = Object.assign({}, state);
        const reports = Object.assign({}, state.reports);
        reports[action.meta.templateId] = {
          jsonData: action.payload,
          syncDate: new Date(),
        };
        newState.reports = reports;
        return newState;
      }
      return state;
    case FETCH_REPORT_TEMPLATES:
      if (action.payload && action.payload.length) {
        const newState = Object.assign({}, state);
        newState.templates = action.payload;
        return newState;
      }
      return state;
    default:
      return state;
  }
}
