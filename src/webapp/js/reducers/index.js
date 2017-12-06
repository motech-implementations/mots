import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import authReducer from './auth_reducer';
import chwTableReducer from './chw_table_reducer';
import locationsReducer from './locations_reducer';

const rootReducer = combineReducers({
  form: formReducer,
  auth: authReducer,
  chwList: chwTableReducer,
  availableLocations: locationsReducer,
});

export default rootReducer;
