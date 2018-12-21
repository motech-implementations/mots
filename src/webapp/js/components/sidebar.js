import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import {
  ASSIGN_MODULES_AUTHORITY, CHW_READ_AUTHORITY, CHW_WRITE_AUTHORITY,
  hasAuthority,
  INCHARGE_READ_AUTHORITY,
  INCHARGE_WRITE_AUTHORITY,
  MANAGE_MODULES_AUTHORITY, DISPLAY_REPORTS_AUTHORITY, MANAGE_USERS_AUTHORITY,
  CREATE_FACILITIES_AUTHORITY, DISPLAY_FACILITIES_AUTHORITY,
  DISPLAY_MODULES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY, MANAGE_FACILITIES_AUTHORITY,
  MANAGE_INCHARGE_USERS_AUTHORITY, UPLOAD_CHW_OR_INCHARGE_CSV_AUTHORITY,
  UPLOAD_LOCATION_CSV_AUTHORITY,
} from '../utils/authorization';
import { fetchReports } from '../actions/index';

class SideBar extends Component {
  constructor(props) {
    super(props);

    this.state = {
      healthWorkersMenuCollapsed: true,
      modulesMenuCollapsed: true,
      inchargeMenuCollapsed: true,
      locationsMenuCollapsed: true,
      usersMenuCollapsed: true,
      reportsMenuCollapsed: true,
    };

    this.toggleHealthWorkersMenu = this.toggleHealthWorkersMenu.bind(this);
    this.toggleModulesMenu = this.toggleModulesMenu.bind(this);
    this.toggleInchargeMenu = this.toggleInchargeMenu.bind(this);
    this.toggleLocationsMenu = this.toggleLocationsMenu.bind(this);
    this.toggleUsersMenu = this.toggleUsersMenu.bind(this);
    this.toggleReportsMenu = this.toggleReportsMenu.bind(this);
  }

  componentWillMount() {
    if (hasAuthority(DISPLAY_REPORTS_AUTHORITY)) {
      this.props.fetchReports();
    }
  }

  static getSubmenuArrowClass(collapsed) {
    if (collapsed) {
      return 'fa fa-angle-left pull-right';
    }

    return 'fa fa-angle-down pull-right';
  }

  toggleHealthWorkersMenu(event) {
    event.preventDefault();
    this.setState({ healthWorkersMenuCollapsed: !this.state.healthWorkersMenuCollapsed });
    return false;
  }

  toggleModulesMenu(event) {
    event.preventDefault();
    this.setState({ modulesMenuCollapsed: !this.state.modulesMenuCollapsed });
    return false;
  }

  toggleInchargeMenu(event) {
    event.preventDefault();
    this.setState({ inchargeMenuCollapsed: !this.state.inchargeMenuCollapsed });
    return false;
  }

  toggleLocationsMenu(event) {
    event.preventDefault();
    this.setState({ locationsMenuCollapsed: !this.state.locationsMenuCollapsed });
    return false;
  }

  toggleUsersMenu(event) {
    event.preventDefault();
    this.setState({ usersMenuCollapsed: !this.state.usersMenuCollapsed });
    return false;
  }

  toggleReportsMenu(event) {
    event.preventDefault();
    this.setState({ reportsMenuCollapsed: !this.state.reportsMenuCollapsed });
    return false;
  }

  renderHealthWorkersMenu() {
    if (this.state.healthWorkersMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav nav-second-level">
        { hasAuthority(CHW_WRITE_AUTHORITY) &&
          <li className="border-none">
            <Link to="/chw/new" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-plus" />
              <span className="icon-text">Add CHW</span>
            </Link>
          </li>
        }
        { hasAuthority(UPLOAD_CHW_OR_INCHARGE_CSV_AUTHORITY) &&
          <li className="border-none">
            <Link to="/chw/upload" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-save-file" />
              <span className="icon-text">Upload CSV</span>
            </Link>
          </li>
        }
        { hasAuthority(CHW_READ_AUTHORITY) &&
          <li className="border-none">
            <Link to="/chw/overall" onClick={this.props.hideMenuSmart}><span className="glyphicon glyphicon-list-alt" />
              <span className="icon-text">CHW List</span>
            </Link>
          </li>
        }
        { hasAuthority(CHW_READ_AUTHORITY) &&
        <li className="border-none">
          <Link to="/chw/selected" onClick={this.props.hideMenuSmart}><span className="glyphicon glyphicon-list-alt" />
            <span className="icon-text">Selected CHW List</span>
          </Link>
        </li>
        }
      </ul>
    );
  }

  renderModulesMenu() {
    if (this.state.modulesMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav nav-second-level">
        { hasAuthority(ASSIGN_MODULES_AUTHORITY) &&
          <li className="border-none">
            <Link to="/modules/groupAssign" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-ok" />
              <span className="icon-text">Assign</span>
            </Link>
          </li>
        }
        { hasAuthority(MANAGE_MODULES_AUTHORITY, DISPLAY_MODULES_AUTHORITY) &&
          <li className="border-none">
            <Link to="/modules/manage" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-th-list" />
              <span className="icon-text">{ hasAuthority(MANAGE_MODULES_AUTHORITY) ? 'Manage Modules' : 'Module List' }</span>
            </Link>
          </li>
        }
      </ul>
    );
  }

  renderInchargeMenu() {
    if (this.state.inchargeMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav nav-second-level">
        { hasAuthority(INCHARGE_WRITE_AUTHORITY) &&
          <li className="border-none">
            <Link to="/incharge/new" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-plus" />
              <span className="icon-text">Add Incharge</span>
            </Link>
          </li>
        }
        { hasAuthority(UPLOAD_CHW_OR_INCHARGE_CSV_AUTHORITY) &&
          <li className="border-none">
            <Link to="/incharge/upload" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-save-file" />
              <span className="icon-text">Upload CSV</span>
            </Link>
          </li>
        }
        { hasAuthority(INCHARGE_READ_AUTHORITY) &&
          <li className="border-none">
            <Link to="/incharge/overall" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-list-alt" />
              <span className="icon-text">Incharge list</span>
            </Link>
          </li>
        }
        { hasAuthority(INCHARGE_READ_AUTHORITY) &&
          <li className="border-none">
            <Link to="/incharge/selected" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-list-alt" />
              <span className="icon-text">Selected Incharge list</span>
            </Link>
          </li>
        }
      </ul>
    );
  }

  renderLocationsMenu() {
    if (this.state.locationsMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav nav-second-level">
        { hasAuthority(CREATE_FACILITIES_AUTHORITY) &&
          <li className="border-none">
            <Link to="/locations/community/new" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-plus" />
              <span className="icon-text">Add Community</span>
            </Link>
          </li>
        }
        { hasAuthority(CREATE_FACILITIES_AUTHORITY) &&
          <li className="border-none">
            <Link to="/locations/facility/new" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-plus" />
              <span className="icon-text">Add Facility</span>
            </Link>
          </li>
        }
        { hasAuthority(CREATE_FACILITIES_AUTHORITY) &&
          <li className="border-none">
            <Link to="/locations/chiefdom/new" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-plus" />
              <span className="icon-text">Add Chiefdom</span>
            </Link>
          </li>
        }
        { hasAuthority(CREATE_FACILITIES_AUTHORITY) &&
          <li className="border-none">
            <Link to="/locations/district/new" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-plus" />
              <span className="icon-text">Add District</span>
            </Link>
          </li>
        }
        { hasAuthority(UPLOAD_LOCATION_CSV_AUTHORITY) &&
          <li className="border-none">
            <Link to="/locations/community/upload" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-save-file" />
              <span className="icon-text">Upload Community CSV</span>
            </Link>
          </li>
        }
        { hasAuthority(UPLOAD_LOCATION_CSV_AUTHORITY) &&
          <li className="border-none">
            <Link to="/locations/facility/upload" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-save-file" />
              <span className="icon-text">Upload Facility CSV</span>
            </Link>
          </li>
        }
        { hasAuthority(UPLOAD_LOCATION_CSV_AUTHORITY) &&
          <li className="border-none">
            <Link to="/locations/chiefdom/upload" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-save-file" />
              <span className="icon-text">Upload Chiefdom CSV</span>
            </Link>
          </li>
        }
        { hasAuthority(
          DISPLAY_FACILITIES_AUTHORITY,
          MANAGE_FACILITIES_AUTHORITY,
          MANAGE_OWN_FACILITIES_AUTHORITY,
        ) &&
        <li className="border-none">
          <Link to="/locations/0" onClick={this.props.hideMenuSmart}>
            <span className="glyphicon glyphicon-list-alt" />
            <span className="icon-text">Location list</span>
          </Link>
        </li>
        }
      </ul>
    );
  }

  renderUsersMenu() {
    if (this.state.usersMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav nav-second-level">
        { hasAuthority(MANAGE_USERS_AUTHORITY, MANAGE_INCHARGE_USERS_AUTHORITY) &&
        <li className="border-none">
          <Link to="/users/new" onClick={this.props.hideMenuSmart}>
            <span className="glyphicon glyphicon-plus" />
            <span className="icon-text">Add User</span>
          </Link>
        </li>
        }
        { hasAuthority(MANAGE_USERS_AUTHORITY, MANAGE_INCHARGE_USERS_AUTHORITY) &&
        <li className="border-none">
          <Link to="/users" onClick={this.props.hideMenuSmart}>
            <span className="glyphicon glyphicon-list-alt" />
            <span className="icon-text">User list</span>
          </Link>
        </li>
        }
        { hasAuthority(MANAGE_USERS_AUTHORITY) &&
        <li className="border-none">
          <Link to="/roles/new" onClick={this.props.hideMenuSmart}>
            <span className="glyphicon glyphicon-plus" />
            <span className="icon-text">Add role</span>
          </Link>
        </li>
        }
        { hasAuthority(MANAGE_USERS_AUTHORITY) &&
        <li className="border-none">
          <Link to="/roles" onClick={this.props.hideMenuSmart}>
            <span className="glyphicon glyphicon-list-alt" />
            <span className="icon-text">Role list</span>
          </Link>
        </li>
        }
      </ul>
    );
  }

  renderReportsMenu() {
    if (this.state.reportsMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav nav-second-level">
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

  render() {
    return (
      <div className={`navbar-collapse ${this.props.showMenuSmart ? '' : 'collapse'}`}>
        <ul className="nav navbar-nav side-nav">
          <li className="hide-min-r-small-min">
            <Link to="/profile" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-user" />
              <span className="icon-text">Profile</span>
            </Link>
          </li>
          <li>
            <Link to="/" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-home" />
              <span className="icon-text">{this.props.showMenuSmart} Home</span>
            </Link>
          </li>
          { hasAuthority(
              CHW_READ_AUTHORITY,
              CHW_WRITE_AUTHORITY,
              UPLOAD_CHW_OR_INCHARGE_CSV_AUTHORITY,
            ) &&
            <li>
              <a href="" onClick={this.toggleHealthWorkersMenu}>
                <span className="fa fa-users" />
                <span className="icon-text">CHW</span>
                <span
                  className={SideBar.getSubmenuArrowClass(this.state.healthWorkersMenuCollapsed)}
                />
              </a>
              {this.renderHealthWorkersMenu()}
            </li>
          }
          { hasAuthority(
              ASSIGN_MODULES_AUTHORITY,
              MANAGE_MODULES_AUTHORITY,
              DISPLAY_MODULES_AUTHORITY,
          ) &&
            <li>
              <a href="" onClick={this.toggleModulesMenu}>
                <span className="glyphicon glyphicon-education" />
                <span className="icon-text">Modules</span>
                <span
                  className={SideBar.getSubmenuArrowClass(this.state.modulesMenuCollapsed)}
                />
              </a>
              { this.renderModulesMenu() }
            </li>
          }
          { hasAuthority(
              INCHARGE_READ_AUTHORITY,
              INCHARGE_WRITE_AUTHORITY,
              UPLOAD_CHW_OR_INCHARGE_CSV_AUTHORITY,
            ) &&
            <li>
              <a href="" onClick={this.toggleInchargeMenu}>
                <span className="fa fa-user-md" />
                <span className="icon-text">Incharge</span>
                <span
                  className={SideBar.getSubmenuArrowClass(this.state.inchargeMenuCollapsed)}
                />
              </a>
              {this.renderInchargeMenu()}
            </li>
          }
          { hasAuthority(DISPLAY_REPORTS_AUTHORITY) &&
          <li>
            <a href="" onClick={this.toggleReportsMenu}>
              <span className="fa fa-bar-chart" />
              <span className="icon-text">Reports</span>
              <span
                className={SideBar.getSubmenuArrowClass(this.state.reportsMenuCollapsed)}
              />
            </a>
            {this.renderReportsMenu()}
          </li>
          }
          { hasAuthority(
            DISPLAY_FACILITIES_AUTHORITY,
            MANAGE_FACILITIES_AUTHORITY,
            MANAGE_OWN_FACILITIES_AUTHORITY,
            CREATE_FACILITIES_AUTHORITY,
            UPLOAD_LOCATION_CSV_AUTHORITY,
          ) &&
          <li>
            <a href="" onClick={this.toggleLocationsMenu}>
              <span className="fa fa-map-marker" />
              <span className="icon-text">Locations</span>
              <span
                className={SideBar.getSubmenuArrowClass(this.state.usersMenuCollapsed)}
              />
            </a>
            {this.renderLocationsMenu()}
          </li>
          }
          { hasAuthority(MANAGE_USERS_AUTHORITY, MANAGE_INCHARGE_USERS_AUTHORITY) &&
          <li>
            <a href="" onClick={this.toggleUsersMenu}>
              <span className="glyphicon glyphicon-user" />
              <span className="icon-text">Users</span>
              <span
                className={SideBar.getSubmenuArrowClass(this.state.usersMenuCollapsed)}
              />
            </a>
            {this.renderUsersMenu()}
          </li>
          }
          <li className="hide-min-r-small-min">
            <a href="" onClick={this.props.signoutUser}>
              <span className="glyphicon glyphicon-log-in" /> Logout
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
