import ReduxPromise from 'redux-promise';
import reduxThunk from 'redux-thunk';
import { createStore, applyMiddleware, compose } from 'redux';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import { offline } from 'redux-offline';
import offlineConfig from 'redux-offline/lib/defaults';

import reducers from './reducers';

const persistConfig = {
  key: 'root',
  storage,
};

const persistedReducer = persistReducer(persistConfig, reducers);

const store = createStore(
  persistedReducer,
  compose(
    applyMiddleware(reduxThunk, ReduxPromise),
    offline(offlineConfig),
  ),
);

export default store;
