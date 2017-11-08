import React, { Component } from 'react';

export default class SideBar extends Component {

  render() {
    return (
      <div>
        <nav id="sidebar">
            <ul className="sidebar-nav">
              <li className="active">
                <a href="#"><span className="glyphicon glyphicon-home"/> Home</a>
              </li>
              <li>
                <a href="#"><span className="fa fa-users"/> CHW</a>
              </li>
              <li>
                <a href="#"><span className="glyphicon glyphicon-education"/> Modules</a>
              </li>
              <li>
                <a href="#"><span className="fa fa-bar-chart"/> Reports</a>
              </li>
            </ul>
        </nav>
      </div>
    );
  }
}