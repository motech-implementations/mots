import React from 'react';
import { Provider } from 'react-redux';
import { Scene, Router, Modal } from 'react-native-router-flux';
import { View, StatusBar, NetInfo } from 'react-native';

import Header from './components/Header';
import ModalInfo from './components/ModalInfo';
import ModalError from './components/ModalError';
import ModalConfirm from './components/ModalConfirm';
import ModalSuccess from './components/ModalSuccess';
import Home from './components/Home';
import Incharges from './components/Incharges';
import InchargesOverall from './components/InchargesOverall';
import AssignModulesToChw from './components/AssignModulesToChw';
import AssignModulesToDistrict from './components/AssignModulesToDistrict';
import InchargesNew from './components/InchargesNew';
import InchargesEdit from './components/InchargesEdit';
import HealthWorkers from './components/HealthWorkers';
import HealthWorkersOverall from './components/HealthWorkersOverall';
import HealthWorkersNew from './components/HealthWorkersNew';
import SynchronizeView from './components/SynchronizeView';
import HealthWorkersEdit from './components/HealthWorkersEdit';
import Users from './components/Users';
import UserNew from './components/UserNew';
import UserEdit from './components/UserEdit';
import Store from './store';
import AppDrawer from './components/AppDrawer';
import Login from './container/Login';
import requireAuth from './components/auth/RequireAuth';
import Report from './components/Report';
import ProfileEdit from './components/ProfileEdit';
import { setConnectionState } from './actions';

export const { dispatch } = Store;

const App = () => (
  <View style={{ flex: 1 }}>
    <StatusBar backgroundColor="black" barStyle="light-content" />
    <Provider store={Store}>
      <Router>
        <Scene key="modal" component={Modal}>
          <Scene key="auth">
            <Scene key="login" component={Login} hideNavBar />
          </Scene>
          <Scene key="drawer" component={requireAuth(AppDrawer)} initial open={false}>
            <Scene key="main">
              <Scene key="home" component={Home} title="Home" hideNavBar={false} navBar={Header} initial />
              <Scene key="profile" component={ProfileEdit} title="Edit profile" navBar={Header} />
              <Scene key="incharges" component={Incharges} title="Selected Incharge List" hideNavBar={false} navBar={Header} />
              <Scene key="allIncharges" component={InchargesOverall} title="Incharge List" hideNavBar={false} navBar={Header} />
              <Scene key="inchargesNew" component={InchargesNew} title="Add Incharge" navBar={Header} />
              <Scene key="inchargesEdit" component={InchargesEdit} title="Edit Incharge" navBar={Header} />
              <Scene key="modulesToChw" component={AssignModulesToChw} title="Assign Modules" navBar={Header} />
              <Scene key="modulesToDistrict" component={AssignModulesToDistrict} title="Assign Modules" navBar={Header} />
              <Scene key="chws" component={HealthWorkers} title="Selected Community Health Workers" navBar={Header} />
              <Scene key="allChws" component={HealthWorkersOverall} title="Community Health Workers" navBar={Header} />
              <Scene key="chwsNew" component={HealthWorkersNew} title="Add Community Health Worker" navBar={Header} />
              <Scene key="chwsEdit" component={HealthWorkersEdit} title="Edit Community Health Worker" navBar={Header} />
              <Scene key="users" component={Users} title="User List" hideNavBar={false} navBar={Header} />
              <Scene key="userNew" component={UserNew} title="Add New User" navBar={Header} />
              <Scene key="userEdit" component={UserEdit} title="Edit User" navBar={Header} />
              <Scene key="synchronizeView" component={SynchronizeView} title="Synchronize" navBar={Header} />
              <Scene key="report" component={Report} title="Download Report" hideNavBar={false} navBar={Header} />
            </Scene>
          </Scene>
          <Scene key="modalInfo" component={ModalInfo} hideNavBar />
          <Scene key="modalError" component={ModalError} hideNavBar />
          <Scene key="modalConfirm" component={ModalConfirm} hideNavBar />
          <Scene key="modalSuccess" component={ModalSuccess} hideNavBar />
        </Scene>
      </Router>
    </Provider>
  </View>
);

NetInfo.getConnectionInfo().then(connInfo => dispatch(setConnectionState(connInfo)));
NetInfo.addEventListener(
  'connectionChange',
  connInfo => dispatch(setConnectionState(connInfo)),
);

export default App;
