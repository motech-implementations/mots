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

export const persistOptions = {
  blacklist: ['connection'],
};

const persistConfig = {
  key: 'root',
  storage,
};

const connectionPersistConfig = {
  key: 'connection',
  storage,
};

const rootReducer = combineReducers({
  auth: authReducer,
  tablesReducer: tablesDataReducer,
  availableLocations: locationsReducer,
  form: formReducer,
  reportReducer,
  connectionReducer: persistReducer(connectionPersistConfig, connectionReducer),
  sceneReducer,
});
const persistedReducers = persistReducer(persistConfig, rootReducer);

export default persistedReducers;
