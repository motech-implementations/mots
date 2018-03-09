import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import Alert from 'react-s-alert';

import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/bouncyflip.css';

import Navbar from './navbar';
import Home from './home';
import Incharges from './incharges';
import HealthWorkers from './health-workers';
import HealthWorkersNew from './health-workers-new';
import HealthWorkersEdit from './health-workers-edit';
import AssignModules from './assign-modules';
import InchargeNew from './incharge-new';
import InchargeEdit from './incharge-edit';
import ModulesManage from './modules-manage';
import Users from './users';
import UserNew from './user-new';
import UserEdit from './user-edit';
import Locations from './locations';
import FacilityNew from './facility-new';
import FacilityEdit from './facility-edit';
import CommunityNew from './community-new';
import CommunityEdit from './community-edit';
import UserProfileEdit from './user-profile-edit';
import GroupAssignModules from './group-assign-modules';
import Report from './report';

export default class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showMenuSmart: false,
    };

    this.toggleShowMenuSmart = this.toggleShowMenuSmart.bind(this);
    this.hideMenuSmart = this.hideMenuSmart.bind(this);
  }

  toggleShowMenuSmart() {
    this.setState({
      showMenuSmart: !this.state.showMenuSmart,
    });
  }

  hideMenuSmart() {
    this.setState({
      showMenuSmart: false,
    });
  }

  render() {
    return (
      <div id="wrapper">
        <Navbar
          showMenuSmart={this.state.showMenuSmart}
          toggleShowMenuSmart={this.toggleShowMenuSmart}
          hideMenuSmart={this.hideMenuSmart}
        />
        <div id="page-wrapper" className={this.state.showMenuSmart ? 'hide-max-r-xsmall-max' : ''}>
          <div className="container-wrapper">
            <div className="container-fluid">
              <Switch>
                <Route path="/modules/assign/:chwId?" component={AssignModules} />
                <Route path="/modules/groupAssign" component={GroupAssignModules} />
                <Route path="/modules/manage" component={ModulesManage} />
                <Route path="/incharge/new" component={InchargeNew} />
                <Route path="/incharge/:inchargeId" component={InchargeEdit} />
                <Route path="/incharge" component={Incharges} />
                <Route path="/chw/new" component={HealthWorkersNew} />
                <Route path="/chw/:chwId" component={HealthWorkersEdit} />
                <Route path="/chw" component={HealthWorkers} />
                <Route path="/report/:reportId" exact component={Report} />
                <Route path="/users/new" component={UserNew} />
                <Route path="/users/:userId" component={UserEdit} />
                <Route path="/users" component={Users} />
                <Route path="/locations/facility/new" component={FacilityNew} />
                <Route path="/locations/facility/:facilityId" component={FacilityEdit} />
                <Route path="/locations/community/new" component={CommunityNew} />
                <Route path="/locations/community/:communityId" component={CommunityEdit} />
                <Route path="/locations" component={Locations} />
                <Route path="/profile" component={UserProfileEdit} />
                <Route path="/" component={Home} />
              </Switch>
            </div>
          </div>
        </div>

        <Alert
          timeout='none'
          stack={{ limit: 3 }}
          offset={29.5}
          html
          position="top-right"
          effect="bouncyflip"
        />
      </div>
    );
  }
}
