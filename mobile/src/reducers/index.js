import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import authReducer from './auth_reducer';
import tablesDataReducer from './tables-data/tables_data_reducer';
import locationsReducer from './locations_reducer';
import reportReducer from './report_reducer';
import connectionReducer from './connection_reducer';
import sceneReducer from './scene_reducer';
import persistenceReducer from './persistence_reducer';
import lastOnlineReducer from './last_online_reducer';

export const persistOptions = {
  blacklist: ['connectionReducer'],
};

const persistConfig = {
  key: 'root',
  storage,
};

const rootReducer = combineReducers({
  auth: authReducer,
  tablesReducer: tablesDataReducer,
  availableLocations: locationsReducer,
  form: formReducer,
  reportReducer,
  connectionReducer,
  sceneReducer,
  persistenceReducer,
  lastOnlineReducer,
});
const persistedReducers = persistReducer(persistConfig, rootReducer);

export default persistedReducers;
