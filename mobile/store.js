import ReduxPromise from 'redux-promise';
import reduxThunk from 'redux-thunk';
import { createStore, applyMiddleware, compose } from 'redux';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import { offline } from 'redux-offline';
import defaultConfig from 'redux-offline/lib/defaults';
import apiClient from './utils/api-client';

import reducers from './reducers';

const persistConfig = {
  key: 'root',
  storage,
};

const myOfflineConfig = {
  ...defaultConfig,
  effect: effect => apiClient.send(effect),
};

const persistedReducer = persistReducer(persistConfig, reducers);

const store = createStore(
  persistedReducer,
  compose(
    applyMiddleware(reduxThunk, ReduxPromise),
    offline(myOfflineConfig),
  ),
);

export default store;
