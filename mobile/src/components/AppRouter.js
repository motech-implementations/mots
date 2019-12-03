import React from 'react';
import { connect } from 'react-redux';
import { Modal, Router, Scene, Actions } from 'react-native-router-flux';
import PropTypes from 'prop-types';

import Header from './Header';
import ModalInfo from './ModalInfo';
import ModalError from './ModalError';
import ModalConfirm from './ModalConfirm';
import ModalSuccess from './ModalSuccess';
import Home from './Home';
import AssignModulesToChw from './AssignModulesToChw';
import AssignModulesToLocation from './AssignModulesToLocation';
import AssignModulesToGroup from './AssignModulesToGroup';
import HealthWorkers from './HealthWorkers';
import HealthWorkersOverall from './HealthWorkersOverall';
import HealthWorkersNew from './HealthWorkersNew';
import SynchronizeView from './SynchronizeView';
import HealthWorkersEdit from './HealthWorkersEdit';
import Users from './Users';
import UserNew from './UserNew';
import UserEdit from './UserEdit';
import AppDrawer from './AppDrawer';
import Login from '../container/Login';
import requireAuth from './auth/RequireAuth';
import Report from './Report';
import ProfileEdit from './ProfileEdit';
import keyboardAvoiding from './KeyboardAvoiding'

const ConnectedRouter = connect()(Router);

// --- Create it via Actions.create(), or it will be re-created for each render of your Router
const scenes = Actions.create(
  <Scene key="modal" component={Modal}>
    <Scene key="auth">
      <Scene key="login" component={keyboardAvoiding(Login)} hideNavBar />
    </Scene>
    <Scene key="drawer" component={requireAuth(AppDrawer)} initial open={false}>
      <Scene key="main">
        <Scene key="home" component={Home} title="Home" hideNavBar={false} navBar={Header} initial />
        <Scene key="profile" component={keyboardAvoiding(ProfileEdit)} title="Edit profile" navBar={Header} />
        <Scene key="modulesToChw" component={AssignModulesToChw} title="Assign Modules" navBar={Header} />
        <Scene key="modulesToLocation" component={AssignModulesToLocation} title="Assign Modules to a location" navBar={Header} />
        <Scene key="modulesToGroup" component={AssignModulesToGroup} title="Assign Modules to a Group" navBar={Header} />
        <Scene key="chws" component={HealthWorkers} title="Selected Community Health Workers" navBar={Header} />
        <Scene key="allChws" component={HealthWorkersOverall} title="Community Health Workers" navBar={Header} />
        <Scene key="chwsNew" component={keyboardAvoiding(HealthWorkersNew)} title="Add Community Health Worker" navBar={Header} />
        <Scene key="chwsEdit" component={keyboardAvoiding(HealthWorkersEdit)} title="Edit Community Health Worker" navBar={Header} />
        <Scene key="users" component={Users} title="User List" hideNavBar={false} navBar={Header} />
        <Scene key="userNew" component={keyboardAvoiding(UserNew)} title="Add New User" navBar={Header} />
        <Scene key="userEdit" component={keyboardAvoiding(UserEdit)} title="Edit User" navBar={Header} />
        <Scene key="synchronizeView" component={SynchronizeView} title="Synchronize" navBar={Header} />
        <Scene key="report" component={Report} title="Download Report" hideNavBar={false} navBar={Header} />
      </Scene>
    </Scene>
    <Scene key="modalInfo" component={ModalInfo} hideNavBar />
    <Scene key="modalError" component={ModalError} hideNavBar />
    <Scene key="modalConfirm" component={ModalConfirm} hideNavBar />
    <Scene key="modalSuccess" component={ModalSuccess} hideNavBar />
  </Scene>);

function AppRouter(props) {
  if (props.rehydrated) {
    return (
      <ConnectedRouter scenes={scenes} />
    );
  }
  return null;
}

function mapStateToProps(state) {
  return {
    rehydrated: state.persistenceReducer.rehydrated,
  };
}

export default connect(mapStateToProps, { })(AppRouter);

AppRouter.propTypes = {
  rehydrated: PropTypes.bool.isRequired,
};
