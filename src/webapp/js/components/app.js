import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';

import Navbar from './navbar';
import Home from './home';
import InchargesOverall from './incharges-overall';
import InchargesSelected from './incharges-selected';
import InchargesUpload from './incharge-upload';
import HealthWorkersOverall from './health-workers-overall';
import HealthWorkersSelected from './health-workers-selected';
import HealthWorkersNew from './health-workers-new';
import HealthWorkersEdit from './health-workers-edit';
import HealthWorkersUpload from './health-workers-upload';
import AssignModules from './assign-modules';
import InchargeNew from './incharge-new';
import InchargeEdit from './incharge-edit';
import ModulesManage from './modules-manage';
import Users from './users';
import UserNew from './user-new';
import UserEdit from './user-edit';
import Roles from './roles';
import RoleNew from './role-new';
import RoleEdit from './role-edit';
import Locations from './locations';
import FacilityNew from './facility-new';
import FacilityEdit from './facility-edit';
import CommunityNew from './community-new';
import CommunityEdit from './community-edit';
import ChiefdomNew from './chiefdom-new';
import ChiefdomEdit from './chiefdom-edit';
import DistrictNew from './district-new';
import DistrictEdit from './district-edit';
import UserProfileEdit from './user-profile-edit';
import GroupAssignModules from './group-assign-modules';
import Report from './report';
import ChiefdomUpload from './chiefdom-upload';
import FacilityUpload from './facility-upload';
import CommunityUpload from './community-upload';
import Groups from './groups';
import GroupNew from './group-new';
import GroupEdit from './group-edit';

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
                <Route path="/incharge/upload" component={InchargesUpload} />
                <Route path="/incharge/new" component={InchargeNew} />
                <Route path="/incharge/selected" component={InchargesSelected} />
                <Route path="/incharge/overall" component={InchargesOverall} />
                <Route path="/incharge/:inchargeId" component={InchargeEdit} />
                <Route path="/chw/upload" component={HealthWorkersUpload} />
                <Route path="/chw/new" component={HealthWorkersNew} />
                <Route path="/chw/selected" component={HealthWorkersSelected} />
                <Route path="/chw/overall" component={HealthWorkersOverall} />
                <Route path="/chw/:chwId" component={HealthWorkersEdit} />
                <Route path="/report/:reportId" exact component={Report} />
                <Route path="/users/new" component={UserNew} />
                <Route path="/users/:userId" component={UserEdit} />
                <Route path="/users" component={Users} />
                <Route path="/roles/new" component={RoleNew} />
                <Route path="/roles/:roleId" component={RoleEdit} />
                <Route path="/roles" component={Roles} />
                <Route path="/locations/facility/upload" component={FacilityUpload} />
                <Route path="/locations/facility/new" component={FacilityNew} />
                <Route path="/locations/facility/:facilityId" component={FacilityEdit} />
                <Route path="/locations/community/upload" component={CommunityUpload} />
                <Route path="/locations/community/new" component={CommunityNew} />
                <Route path="/locations/community/:communityId" component={CommunityEdit} />
                <Route path="/locations/chiefdom/upload" component={ChiefdomUpload} />
                <Route path="/locations/chiefdom/new" component={ChiefdomNew} />
                <Route path="/locations/chiefdom/:chiefdomId" component={ChiefdomEdit} />
                <Route path="/locations/district/new" component={DistrictNew} />
                <Route path="/locations/district/:districtId" component={DistrictEdit} />
                <Route path="/locations/:tabIndex?" component={Locations} />
                <Route path="/profile" component={UserProfileEdit} />
                <Route path="/groups/new" component={GroupNew} />
                <Route path="/groups/:groupId" component={GroupEdit} />
                <Route path="/groups" component={Groups} />
                <Route path="/" component={Home} />
              </Switch>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
