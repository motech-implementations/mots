import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import authReducer from './auth_reducer';
import tablesDataReducer from './tables-data/tables_data_reducer';
import locationsReducer from './locations_reducer';

const rootReducer = combineReducers({
  auth: authReducer,
  tablesReducer: tablesDataReducer,
  availableLocations: locationsReducer,
  selectableLocationsForIncharge: locationsReducer,
  form: formReducer,
});

export default rootReducer;
