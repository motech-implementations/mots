import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import {
  ASSIGN_MODULES_AUTHORITY, CHW_READ_AUTHORITY, CHW_WRITE_AUTHORITY,
  hasAuthority,
  INCHARGE_READ_AUTHORITY,
  INCHARGE_WRITE_AUTHORITY,
  MANAGE_MODULES_AUTHORITY, DISPLAY_REPORTS_AUTHORITY, MANAGE_USERS_AUTHORITY,
  MANAGE_FACILITIES_AUTHORITY, DISPLAY_FACILITIES_AUTHORITY,
  DISPLAY_MODULES_AUTHORITY,
} from '../utils/authorization';

export default class SideBar extends Component {
  static getSubmenuArrowClass(collapsed) {
    if (collapsed) {
      return 'fa fa-angle-left pull-right';
    }

    return 'fa fa-angle-down pull-right';
  }

  constructor(props) {
    super(props);

    this.state = {
      healthWorkersMenuCollapsed: true,
      modulesMenuCollapsed: true,
      inchargeMenuCollapsed: true,
      usersMenuCollapsed: true,
    };

    this.toggleHealthWorkersMenu = this.toggleHealthWorkersMenu.bind(this);
    this.toggleModulesMenu = this.toggleModulesMenu.bind(this);
    this.toggleInchargeMenu = this.toggleInchargeMenu.bind(this);
    this.toggleUsersMenu = this.toggleUsersMenu.bind(this);
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

  toggleUsersMenu(event) {
    event.preventDefault();
    this.setState({ usersMenuCollapsed: !this.state.usersMenuCollapsed });
    return false;
  }

  renderHealthWorkersMenu() {
    if (this.state.healthWorkersMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav nav-second-level">
        { hasAuthority(CHW_WRITE_AUTHORITY) &&
          <li>
            <Link to="/chw/new" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-plus" />
              <span className="icon-text">Add CHW</span>
            </Link>
          </li>
        }
        { hasAuthority(CHW_READ_AUTHORITY) &&
          <li className="border-none">
            <Link to="/chw" onClick={this.props.hideMenuSmart}><span className="glyphicon glyphicon-list-alt" />
              <span className="icon-text">CHW List</span>
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
              <span className="icon-text">Manage Modules</span>
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
        { hasAuthority(INCHARGE_READ_AUTHORITY) &&
          <li className="border-none">
            <Link to="/incharge" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-list-alt" />
              <span className="icon-text">Incharge list</span>
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
        { hasAuthority(MANAGE_USERS_AUTHORITY) &&
        <li className="border-none">
          <Link to="/users" onClick={this.props.hideMenuSmart}>
            <span className="glyphicon glyphicon-plus" />
            <span className="icon-text">Add User</span>
          </Link>
        </li>
        }
        { hasAuthority(MANAGE_USERS_AUTHORITY) &&
        <li className="border-none">
          <Link to="/users" onClick={this.props.hideMenuSmart}>
            <span className="glyphicon glyphicon-list-alt" />
            <span className="icon-text">Users list</span>
          </Link>
        </li>
        }
      </ul>
    );
  }

  render() {
    return (
      <div className={`navbar-collapse ${this.props.showMenuSmart ? '' : 'collapse'}`}>
        <ul className="nav navbar-nav side-nav">
          <li className="hide-min-r-small-min">
            <Link to="/" onClick={this.props.hideMenuSmart}>
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
          { hasAuthority(CHW_READ_AUTHORITY, CHW_WRITE_AUTHORITY) &&
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
          { hasAuthority(INCHARGE_READ_AUTHORITY, INCHARGE_WRITE_AUTHORITY) &&
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
            <Link to="/" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-bar-chart" />
              <span className="icon-text">Reports</span>
            </Link>
          </li>
          }
          { hasAuthority(DISPLAY_FACILITIES_AUTHORITY, MANAGE_FACILITIES_AUTHORITY) &&
          <li>
            <Link to="/locations" onClick={this.props.hideMenuSmart}>
              <span className="fa fa-map-marker  " />
              <span className="icon-text">Locations</span>
            </Link>
          </li>
          }
          { hasAuthority(MANAGE_USERS_AUTHORITY) &&
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

SideBar.propTypes = {
  signoutUser: PropTypes.func.isRequired,
  showMenuSmart: PropTypes.bool,
  hideMenuSmart: PropTypes.func.isRequired,
};

SideBar.defaultProps = {
  showMenuSmart: false,
};
