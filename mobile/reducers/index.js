import { combineReducers } from 'redux';
import authReducer from './auth_reducer';
import tablesDataReducer from './tables-data/tables_data_reducer';
import locationsReducer from './locations_reducer';

const rootReducer = combineReducers({
  auth: authReducer,
  tablesReducer: tablesDataReducer,
  availableLocations: locationsReducer,
});

export default rootReducer;
