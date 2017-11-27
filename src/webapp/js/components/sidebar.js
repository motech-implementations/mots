import React, { Component } from 'react';
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
          <Link to="/chw/new"><span className="glyphicon glyphicon-plus" /> Add CHW</Link>
        </li>
        <li>
          <Link to="/chw"><span className="glyphicon glyphicon-list-alt" /> CHW List</Link>
        </li>
      </ul>
    );
  }

  render() {
    return (
      <div className="collapse navbar-collapse navbar-ex1-collapse">
        <ul className="nav navbar-nav side-nav">
          <li>
            <Link to="/"><span className="glyphicon glyphicon-home" /> Home</Link>
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
            <Link to="/chw"><span className="glyphicon glyphicon-education" /> Modules</Link>
          </li>
          <li>
            <Link to="/"><span className="fa fa-bar-chart" /> Reports</Link>
          </li>
        </ul>
      </div>
    );
  }
}
