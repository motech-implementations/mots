import React, { Component } from 'react';
import { Link } from 'react-router-dom';

export default class SideBar extends Component {

  constructor(props) {
    super(props);

    this.state = { healthWorkersMenuCollapsed: true }
  }

  toggleHealthWorkersMenu(event) {
    event.preventDefault();
    this.setState({ healthWorkersMenuCollapsed: !this.state.healthWorkersMenuCollapsed });
    return false;
  }

  renderHealthWorkersMenu() {
    if (this.state.healthWorkersMenuCollapsed) {
      return "";
    }

    return (
      <ul className="nav nav-second-level">
        <li>
          <Link to="/chw/new"><span className="glyphicon glyphicon-plus"/> Add CHW</Link>
        </li>
        <li>
          <Link to="/chw"><span className="glyphicon glyphicon-list-alt"/> CHW List</Link>
        </li>
      </ul>
    );
  }

  getSubmenuArrowClass(collapsed) {
    if (collapsed) {
      return "fa fa-angle-left pull-right";
    }

    return "fa fa-angle-down pull-right";
  }

  render() {
    return (
        <div>
          <nav id="sidebar">
            <ul className="sidebar-nav">
              <li className="active">
                <Link to="/"><span className="glyphicon glyphicon-home"/> Home</Link>
              </li>
              <li>
                <a onClick={ this.toggleHealthWorkersMenu.bind(this) }>
                  <span className="fa fa-users"/> CHW
                  <span className={ this.getSubmenuArrowClass(this.state.healthWorkersMenuCollapsed) } />
                </a>
                { this.renderHealthWorkersMenu() }
              </li>
              <li>
                <Link to="/"><span className="glyphicon glyphicon-education"/> Modules</Link>
              </li>
              <li>
                <Link to="/"><span className="fa fa-bar-chart"/> Reports</Link>
              </li>
            </ul>
          </nav>
        </div>
    );
  }
}
