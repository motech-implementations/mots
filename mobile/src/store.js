import ReduxPromise from 'redux-promise';
import reduxThunk from 'redux-thunk';
import { createStore, applyMiddleware, compose } from 'redux';
import { offline } from '@redux-offline/redux-offline';
import defaultConfig from '@redux-offline/redux-offline/lib/defaults';
import apiClient from './utils/api-client';

import reducers from './reducers';

const myOfflineConfig = {
  ...defaultConfig,
  effect: effect => apiClient.send(effect),
};

const store = createStore(
  reducers,
  compose(
    applyMiddleware(reduxThunk, ReduxPromise),
    offline(myOfflineConfig),
  ),
);

export default store;
