import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import {
  ASSIGN_MODULES_AUTHORITY, CHW_READ_AUTHORITY, CHW_WRITE_AUTHORITY,
  hasAuthority,
  MANAGE_MODULES_AUTHORITY, DISPLAY_REPORTS_AUTHORITY, MANAGE_USERS_AUTHORITY,
  MANAGE_ROLES_AUTHORITY, CREATE_FACILITIES_AUTHORITY, DISPLAY_FACILITIES_AUTHORITY,
  DISPLAY_MODULES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY, MANAGE_FACILITIES_AUTHORITY,
  UPLOAD_CHW_CSV_AUTHORITY,
  UPLOAD_LOCATION_CSV_AUTHORITY, GROUP_READ_AUTHORITY, GROUP_WRITE_AUTHORITY,
} from '../utils/authorization';
import { fetchReports } from '../actions/index';

class SideBar extends Component {
  constructor(props) {
    super(props);

    this.state = {
      healthWorkersMenuCollapsed: true,
      modulesMenuCollapsed: true,
      locationsMenuCollapsed: true,
      usersMenuCollapsed: true,
      reportsMenuCollapsed: true,
      groupsMenuCollapsed: true,
    };

    this.toggleHealthWorkersMenu = this.toggleHealthWorkersMenu.bind(this);
    this.toggleModulesMenu = this.toggleModulesMenu.bind(this);
    this.toggleLocationsMenu = this.toggleLocationsMenu.bind(this);
    this.toggleUsersMenu = this.toggleUsersMenu.bind(this);
    this.toggleReportsMenu = this.toggleReportsMenu.bind(this);
    this.toggleGroupsMenu = this.toggleGroupsMenu.bind(this);
  }

  componentDidMount() {
    if (hasAuthority(DISPLAY_REPORTS_AUTHORITY)) {
      this.props.fetchReports();
    }
  }

  static getSubmenuArrowClass(collapsed) {
    if (collapsed) {
      return 'fa fa-angle-left float-right';
    }

    return 'fa fa-angle-down float-right';
  }

  toggleHealthWorkersMenu(event) {
    event.preventDefault();
    this.setState(prevState => ({
      healthWorkersMenuCollapsed: !prevState.healthWorkersMenuCollapsed,
    }));
    return false;
  }

  toggleModulesMenu(event) {
    event.preventDefault();
    this.setState(prevState => ({ modulesMenuCollapsed: !prevState.modulesMenuCollapsed }));
    return false;
  }

  toggleLocationsMenu(event) {
    event.preventDefault();
    this.setState(prevState => ({ locationsMenuCollapsed: !prevState.locationsMenuCollapsed }));
    return false;
  }

  toggleUsersMenu(event) {
    event.preventDefault();
    this.setState(prevState => ({ usersMenuCollapsed: !prevState.usersMenuCollapsed }));
    return false;
  }

  toggleReportsMenu(event) {
    event.preventDefault();
    this.setState(prevState => ({ reportsMenuCollapsed: !prevState.reportsMenuCollapsed }));
    return false;
  }

  toggleGroupsMenu(event) {
    event.preventDefault();
    this.setState(prevState => ({ groupsMenuCollapsed: !prevState.groupsMenuCollapsed }));
    return false;
  }

  renderHealthWorkersMenu() {
    if (this.state.healthWorkersMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav-second-level">
        { hasAuthority(CHW_WRITE_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/chw/new" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-plus" />
              <span className="icon-text">Add CHW</span>
            </Link>
          </li>
          )}
        { hasAuthority(UPLOAD_CHW_CSV_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/chw/upload" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-upload" />
              <span className="icon-text">Upload CSV</span>
            </Link>
          </li>
          )}
        { hasAuthority(CHW_READ_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/chw/overall" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-list-alt" />
              <span className="icon-text">CHW List</span>
            </Link>
          </li>
          )}
        { hasAuthority(CHW_READ_AUTHORITY)
        && (
        <li className="border-none">
          <Link to="/chw/selected" onClick={this.props.hideMenuSmart}>
            <span className="fa fa-list-alt" />
            <span className="icon-text">Selected CHW List</span>
          </Link>
        </li>
        )}
      </ul>
    );
  }

  renderModulesMenu() {
    if (this.state.modulesMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav-second-level">
        { hasAuthority(ASSIGN_MODULES_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/modules/groupAssign" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-check" />
              <span className="icon-text">Assign</span>
            </Link>
          </li>
          )}
        { hasAuthority(MANAGE_MODULES_AUTHORITY, DISPLAY_MODULES_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/modules/manage" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-th-list" />
              <span className="icon-text">{ hasAuthority(MANAGE_MODULES_AUTHORITY) ? 'Manage Modules' : 'Module List' }</span>
            </Link>
          </li>
          )}
      </ul>
    );
  }

  renderLocationsMenu() {
    if (this.state.locationsMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav-second-level">
        { hasAuthority(CREATE_FACILITIES_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/locations/village/new" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-plus" />
              <span className="icon-text">Add Village</span>
            </Link>
          </li>
          )}
        { hasAuthority(CREATE_FACILITIES_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/locations/facility/new" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-plus" />
              <span className="icon-text">Add Facility</span>
            </Link>
          </li>
          )}
        { hasAuthority(CREATE_FACILITIES_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/locations/sector/new" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-plus" />
              <span className="icon-text">Add Chiefdom</span>
            </Link>
          </li>
          )}
        { hasAuthority(CREATE_FACILITIES_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/locations/district/new" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-plus" />
              <span className="icon-text">Add District</span>
            </Link>
          </li>
          )}
        { hasAuthority(UPLOAD_LOCATION_CSV_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/locations/village/upload" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-upload" />
              <span className="icon-text">Upload Village CSV</span>
            </Link>
          </li>
          )}
        { hasAuthority(UPLOAD_LOCATION_CSV_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/locations/facility/upload" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-upload" />
              <span className="icon-text">Upload Facility CSV</span>
            </Link>
          </li>
          )}
        { hasAuthority(UPLOAD_LOCATION_CSV_AUTHORITY)
          && (
          <li className="border-none">
            <Link to="/locations/sector/upload" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-upload" />
              <span className="icon-text">Upload Chiefdom CSV</span>
            </Link>
          </li>
          )}
        { hasAuthority(
          DISPLAY_FACILITIES_AUTHORITY,
          MANAGE_FACILITIES_AUTHORITY,
          MANAGE_OWN_FACILITIES_AUTHORITY,
        )
        && (
        <li className="border-none">
          <Link to="/locations/0" onClick={this.props.hideMenuSmart}>
            <span className="fa fa-list-alt" />
            <span className="icon-text">Location list</span>
          </Link>
        </li>
        )}
      </ul>
    );
  }

  renderUsersMenu() {
    if (this.state.usersMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav-second-level">
        { hasAuthority(MANAGE_USERS_AUTHORITY)
        && (
        <li className="border-none">
          <Link to="/users/new" onClick={this.props.hideMenuSmart}>
            <span className="fa fa-plus" />
            <span className="icon-text">Add User</span>
          </Link>
        </li>
        )}
        { hasAuthority(MANAGE_USERS_AUTHORITY)
        && (
        <li className="border-none">
          <Link to="/users" onClick={this.props.hideMenuSmart}>
            <span className="fa fa-list-alt" />
            <span className="icon-text">User list</span>
          </Link>
        </li>
        )}
        { hasAuthority(MANAGE_ROLES_AUTHORITY)
        && (
        <li className="border-none">
          <Link to="/roles/new" onClick={this.props.hideMenuSmart}>
            <span className="fa fa-plus" />
            <span className="icon-text">Add role</span>
          </Link>
        </li>
        )}
        { hasAuthority(MANAGE_ROLES_AUTHORITY)
        && (
        <li className="border-none">
          <Link to="/roles" onClick={this.props.hideMenuSmart}>
            <span className="fa fa-list-alt" />
            <span className="icon-text">Role list</span>
          </Link>
        </li>
        )}
      </ul>
    );
  }

  renderReportsMenu() {
    if (this.state.reportsMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav-second-level">
        {this.props.reportList.map(report => (
          <li key={report.id} className="border-none">
            <Link
              to={{
                pathname: `/report/${report.id}`,
                state: { reportName: report.name },
              }}
              onClick={this.props.hideMenuSmart}
            >
              <span className="fa fa-file-text-o" />
              <span className="icon-text">{report.name}</span>
            </Link>
          </li>
        ))}
      </ul>
    );
  }

  renderGroupsMenu() {
    if (this.state.groupsMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav-second-level">
        { hasAuthority(GROUP_WRITE_AUTHORITY)
        && (
        <li className="border-none">
          <Link to="/groups/new" onClick={this.props.hideMenuSmart}>
            <span className="fa fa-plus" />
            <span className="icon-text">Add Group</span>
          </Link>
        </li>
        )}
        { hasAuthority(GROUP_READ_AUTHORITY)
        && (
        <li className="border-none">
          <Link to="/groups" onClick={this.props.hideMenuSmart}>
            <span className="fa fa-list-alt" />
            <span className="icon-text">Group List</span>
          </Link>
        </li>
        )}
      </ul>
    );
  }

  render() {
    return (
      <div className={`navbar-collapse ${this.props.showMenuSmart ? '' : 'collapse'}`}>
        <ul className="navbar-nav side-nav">
          <li className="hide-min-r-small-min nav-item">
            <Link className="nav-link" to="/profile" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-user" />
              <span className="icon-text">Profile</span>
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-home" />
              <span className="icon-text">
                Home
              </span>
            </Link>
          </li>
          { hasAuthority(
            CHW_READ_AUTHORITY,
            CHW_WRITE_AUTHORITY,
            UPLOAD_CHW_CSV_AUTHORITY,
          )
            && (
            <li className="nav-item">
              <a className="nav-link" href="" onClick={this.toggleHealthWorkersMenu}>
                <span className="fa fa-users" />
                <span className="icon-text">CHW</span>
                <span
                  className={SideBar.getSubmenuArrowClass(this.state.healthWorkersMenuCollapsed)}
                />
              </a>
              {this.renderHealthWorkersMenu()}
            </li>
            )}
          { hasAuthority(
            ASSIGN_MODULES_AUTHORITY,
            MANAGE_MODULES_AUTHORITY,
            DISPLAY_MODULES_AUTHORITY,
          )
            && (
            <li className="nav-item">
              <a className="nav-link" href="" onClick={this.toggleModulesMenu}>
                <span className="fa fa-graduation-cap" />
                <span className="icon-text">Modules</span>
                <span
                  className={SideBar.getSubmenuArrowClass(this.state.modulesMenuCollapsed)}
                />
              </a>
              { this.renderModulesMenu() }
            </li>
            )}
          { hasAuthority(DISPLAY_REPORTS_AUTHORITY)
          && (
          <li className="nav-item">
            <a className="nav-link" href="" onClick={this.toggleReportsMenu}>
              <span className="fa fa-bar-chart" />
              <span className="icon-text">Reports</span>
              <span
                className={SideBar.getSubmenuArrowClass(this.state.reportsMenuCollapsed)}
              />
            </a>
            {this.renderReportsMenu()}
          </li>
          )}
          { hasAuthority(
            DISPLAY_FACILITIES_AUTHORITY,
            MANAGE_FACILITIES_AUTHORITY,
            MANAGE_OWN_FACILITIES_AUTHORITY,
            CREATE_FACILITIES_AUTHORITY,
            UPLOAD_LOCATION_CSV_AUTHORITY,
          )
          && (
          <li className="nav-item">
            <a className="nav-link" href="" onClick={this.toggleLocationsMenu}>
              <span className="fa fa-map-marker" />
              <span className="icon-text">Locations</span>
              <span
                className={SideBar.getSubmenuArrowClass(this.state.usersMenuCollapsed)}
              />
            </a>
            {this.renderLocationsMenu()}
          </li>
          )}
          { hasAuthority(MANAGE_USERS_AUTHORITY, MANAGE_ROLES_AUTHORITY)
          && (
          <li className="nav-item">
            <a className="nav-link" href="" onClick={this.toggleUsersMenu}>
              <span className="fa fa-user" />
              <span className="icon-text">Users</span>
              <span
                className={SideBar.getSubmenuArrowClass(this.state.usersMenuCollapsed)}
              />
            </a>
            {this.renderUsersMenu()}
          </li>
          )}
          { hasAuthority(GROUP_READ_AUTHORITY, GROUP_WRITE_AUTHORITY)
          && (
          <li className="nav-item">
            <a className="nav-link" href="" onClick={this.toggleGroupsMenu}>
              <span className="fa fa-group" />
              <span className="icon-text">Group</span>
              <span
                className={SideBar.getSubmenuArrowClass(this.state.groupsMenuCollapsed)}
              />
            </a>
            {this.renderGroupsMenu()}
          </li>
          )}
          <li className="hide-min-r-small-min nav-item">
            <a className="nav-link" href="" onClick={this.props.signoutUser}>
              <span className="fa fa-sign-out" />
              Logout
            </a>
          </li>
        </ul>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    reportList: state.reports,
  };
}

export default connect(mapStateToProps, { fetchReports })(SideBar);

SideBar.propTypes = {
  signoutUser: PropTypes.func.isRequired,
  showMenuSmart: PropTypes.bool,
  hideMenuSmart: PropTypes.func.isRequired,
  fetchReports: PropTypes.func.isRequired,
  reportList: PropTypes.arrayOf(PropTypes.shape({})),
};

SideBar.defaultProps = {
  showMenuSmart: false,
  reportList: [],
};
