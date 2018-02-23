import React from 'react';
import { Provider } from 'react-redux';
import { Scene, Router } from 'react-native-router-flux';
import Sentry from 'sentry-expo';
import { View, StatusBar } from 'react-native';

import Header from './components/Header';
import Home from './components/Home';
import Incharges from './components/Incharges';
import AssignModulesToChw from './components/AssignModulesToChw';
import AssignModulesToDistrict from './components/AssignModulesToDistrict';
import InchargesNew from './components/InchargesNew';
import InchargesEdit from './components/InchargesEdit';
import HealthWorkers from './components/HealthWorkers';
import HealthWorkersNew from './components/HealthWorkersNew';
import SynchronizeView from './components/SynchronizeView';
import HealthWorkersEdit from './components/HealthWorkersEdit';
import Users from './components/Users';
import Store from './store';
import AppDrawer from './components/AppDrawer';
import Login from './container/Login';
import Config from './config';
import requireAuth from './components/auth/RequireAuth';

Sentry.config(Config.sentryConfig.publicDSN).install();

export const { dispatch } = Store;

const App = () => (
  <View style={{ flex: 1 }}>
    <View style={{ backgroundColor: '#000', height: StatusBar.currentHeight }} />
    <Provider store={Store}>
      <Router>
        <Scene key="auth">
          <Scene key="login" component={Login} hideNavBar />
        </Scene>
        <Scene key="drawer" component={requireAuth(AppDrawer)} initial open={false}>
          <Scene key="main">
            <Scene key="home" component={Home} title="Home" hideNavBar={false} navBar={Header} initial />
            <Scene key="incharges" component={Incharges} title="Incharge List" hideNavBar={false} navBar={Header} />
            <Scene key="inchargesNew" component={InchargesNew} title="Add Incharge" navBar={Header} />
            <Scene key="inchargesEdit" component={InchargesEdit} title="Edit Incharge" navBar={Header} />
            <Scene key="modulesToChw" component={AssignModulesToChw} title="Assign Modules" navBar={Header} />
            <Scene key="modulesToDistrict" component={AssignModulesToDistrict} title="Assign Modules" navBar={Header} />
            <Scene key="chws" component={HealthWorkers} title="Community Health Workers" navBar={Header} />
            <Scene key="chwsNew" component={HealthWorkersNew} title="Add Community Health Worker" navBar={Header} />
            <Scene key="chwsEdit" component={HealthWorkersEdit} title="Edit Community Health Worker" navBar={Header} />
            <Scene key="users" component={Users} title="User List" hideNavBar={false} navBar={Header} />
            <Scene key="synchronizeView" component={SynchronizeView} title="Synchronize" navBar={Header} />
          </Scene>
        </Scene>
      </Router>
    </Provider>
  </View>
);

export default App;
