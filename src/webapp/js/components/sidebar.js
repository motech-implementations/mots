import React, { Component } from 'react';
import { Link } from 'react-router-dom';

export default class SideBar extends Component {

  render() {
    return (
        <div>
          <nav id="sidebar">
            <ul className="sidebar-nav">
              <li className="active">
                <Link to="/"><span className="glyphicon glyphicon-home"/> Home</Link>
              </li>
              <li>
                <Link to="/"><span className="fa fa-users"/> CHW</Link>
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
