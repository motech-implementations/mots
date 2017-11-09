import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

import { signoutUser } from '../actions';

class Header extends Component {

  onSignout(event) {
    this.props.signoutUser();
    event.preventDefault();
  }

  render() {
    return (
        <div>
          <nav className="navbar navbar-default" id='navbar'>
            <div className="container-fluid">
              <div className="navbar-header">
                <Link className="navbar-brand" to="/">MOTS</Link>
              </div>
              <ul className="nav navbar-nav navbar-right">
                <li>
                  <Link to="/">
                    <span className="glyphicon glyphicon-user"/> Profile
                  </Link>
                </li>
                <li>
                  <a href="#" onClick={ this.onSignout.bind(this) }>
                    <span className="glyphicon glyphicon-log-in"/> Logout
                  </a>
                </li>
              </ul>
            </div>
          </nav>
        </div>
    );
  }
}

export default connect(null, { signoutUser })(Header);
