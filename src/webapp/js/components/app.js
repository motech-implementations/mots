import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';

import Navbar from './navbar';
import Home from './home';
import HealthWorkersOverall from './health-workers-overall';
import HealthWorkersSelected from './health-workers-selected';
import HealthWorkersNew from './health-workers-new';
import HealthWorkersEdit from './health-workers-edit';
import HealthWorkersUpload from './health-workers-upload';
import AssignModules from './assign-modules';
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
import VillageNew from './village-new';
import VillageEdit from './village-edit';
import SectorNew from './sector-new';
import SectorEdit from './sector-edit';
import DistrictNew from './district-new';
import DistrictEdit from './district-edit';
import UserProfileEdit from './user-profile-edit';
import GroupAssignModules from './group-assign-modules';
import Report from './report';
import SectorUpload from './sector-upload';
import FacilityUpload from './facility-upload';
import VillageUpload from './village-upload';
import Groups from './groups';
import GroupNew from './group-new';
import GroupEdit from './group-edit';
import AutomatedReports from './automated-reports';
import AutomatedSettingsEdit from './automated-settings-edit';

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
    this.setState(prevState => ({
      showMenuSmart: !prevState.showMenuSmart,
    }));
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
                <Route path="/automatedReports" exact component={AutomatedReports} />
                <Route path="/automatedReportsEdit" exact component={AutomatedSettingsEdit} />
                <Route path="/modules/assign/:chwId?" component={AssignModules} />
                <Route path="/modules/groupAssign" component={GroupAssignModules} />
                <Route path="/modules/manage" component={ModulesManage} />
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
                <Route path="/locations/village/upload" component={VillageUpload} />
                <Route path="/locations/village/new" component={VillageNew} />
                <Route path="/locations/village/:villageId" component={VillageEdit} />
                <Route path="/locations/sector/upload" component={SectorUpload} />
                <Route path="/locations/sector/new" component={SectorNew} />
                <Route path="/locations/sector/:sectorId" component={SectorEdit} />
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
