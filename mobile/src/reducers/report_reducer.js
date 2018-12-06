import { FETCH_REPORT, FETCH_REPORT_TEMPLATES } from '../actions/types';

const initialState = {
  reports: {},
  templates: [],
};

export default function (state = initialState, action) {
  switch (action.type) {
    case FETCH_REPORT: {
      const { payload, meta } = action;
      if (payload && (payload.data || payload.status === 204)) {
        const newState = Object.assign({}, state);
        const reports = Object.assign({}, state.reports);
        const updatedReport = {
          ...reports[meta.templateId],
          syncDate: new Date(),
        };
        if (payload.data) {
          updatedReport.jsonData = payload.data;
          updatedReport.version = payload.version;
        }
        reports[meta.templateId] = updatedReport;
        newState.reports = reports;
        return newState;
      }
      return state;
    }
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
