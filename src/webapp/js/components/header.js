import React, { Component } from 'react';


export default class Header extends Component {

  render() {
    return (
      <div>
        <nav className="navbar navbar-default" id='navbar'>
          <div className="container-fluid">
            <div className="navbar-header">
              <a className="navbar-brand" href="#">MOTS</a>
            </div>
            <ul className="nav navbar-nav navbar-right">
              <li><a href="#">
                <span className="glyphicon glyphicon-user"/> Profile</a>
              </li>
              <li><a href="#">
                <span className="glyphicon glyphicon-log-in"/> Logout</a>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    );
  }
}