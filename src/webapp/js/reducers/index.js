import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import authReducer from './auth_reducer';
import tablesDataReducer from './tables-data/tables_data_reducer';
import locationsReducer from './locations_reducer';
import reportsReducer from './reports_reducer';
import automatedReportsReducer from './automated_reports_reducer';

const rootReducer = combineReducers({
  form: formReducer,
  auth: authReducer,
  tablesReducer: tablesDataReducer,
  availableLocations: locationsReducer,
  reports: reportsReducer,
  automatedReports: automatedReportsReducer,
});

export default rootReducer;
