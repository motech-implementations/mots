import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

export default class SideBar extends Component {
  static getSubmenuArrowClass(collapsed) {
    if (collapsed) {
      return 'fa fa-angle-left pull-right';
    }

    return 'fa fa-angle-down pull-right';
  }

  constructor(props) {
    super(props);

    this.state = { healthWorkersMenuCollapsed: true };

    this.toggleHealthWorkersMenu = this.toggleHealthWorkersMenu.bind(this);
  }

  toggleHealthWorkersMenu(event) {
    event.preventDefault();
    this.setState({ healthWorkersMenuCollapsed: !this.state.healthWorkersMenuCollapsed });
    return false;
  }

  renderHealthWorkersMenu() {
    if (this.state.healthWorkersMenuCollapsed) {
      return '';
    }

    return (
      <ul className="nav nav-second-level">
        <li>
          <Link to="/chw/new" onClick={this.props.hideMenuSmart}><span className="glyphicon glyphicon-plus" /> Add CHW</Link>
        </li>
        <li className="border-none">
          <Link to="/chw" onClick={this.props.hideMenuSmart}><span className="glyphicon glyphicon-list-alt" /> CHW List</Link>
        </li>
      </ul>
    );
  }

  render() {
    return (
      <div className={`navbar-collapse ${this.props.showMenuSmart ? '' : 'collapse'}`}>
        <ul className="nav navbar-nav side-nav">
          <li className="hide-min-r-small-min">
            <Link to="/" onClick={this.props.hideMenuSmart}>
              <span className="glyphicon glyphicon-user" /> Profile
            </Link>
          </li>
          <li>
            <Link to="/" onClick={this.props.hideMenuSmart}><span className="glyphicon glyphicon-home" />{this.props.showMenuSmart} Home</Link>
          </li>
          <li>
            <a href="" onClick={this.toggleHealthWorkersMenu}>
              <span className="fa fa-users" /> CHW
              <span
                className={SideBar.getSubmenuArrowClass(this.state.healthWorkersMenuCollapsed)}
              />
            </a>
            { this.renderHealthWorkersMenu() }
          </li>
          <li>
            <Link to="/chw" onClick={this.props.hideMenuSmart}><span className="glyphicon glyphicon-education" /> Modules</Link>
          </li>
          <li>
            <Link to="/" onClick={this.props.hideMenuSmart}><span className="fa fa-bar-chart" /> Reports</Link>
          </li>
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
