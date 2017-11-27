import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

import { signoutUser } from '../actions';

class Header extends Component {

  onSignout(event) {
    this.props.signoutUser();
    event.preventDefault();
    return false;
  }

  render() {
    return (
        <div>
          <div className="navbar-header">
            <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
              <span className="sr-only">Toggle navigation</span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
            </button>
            <Link className="navbar-brand" to="/">MOTS</Link>
          </div>
          <ul className="nav navbar-right top-nav">
            <li>
              <Link to="/">
                <span className="glyphicon glyphicon-user"/> Profile
              </Link>
            </li>
            <li>
              <a href="" onClick={ this.onSignout.bind(this) }>
                <span className="glyphicon glyphicon-log-in"/> Logout
              </a>
            </li>
          </ul>
        </div>
    );
  }
}

export default connect(null, { signoutUser })(Header);
