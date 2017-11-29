import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

import { signoutUser } from '../actions';

class Header extends Component {
  constructor(props) {
    super(props);

    this.onSignout = this.onSignout.bind(this);
  }

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
            <span className="icon-bar" />
            <span className="icon-bar" />
            <span className="icon-bar" />
          </button>
          <Link className="navbar-brand" to="/">MOTS</Link>
        </div>
        <ul className="nav navbar-right top-nav">
          <li>
            <Link to="/">
              <span className="glyphicon glyphicon-user" /> Profile
            </Link>
          </li>
          <li>
            <a href="" onClick={this.onSignout}>
              <span className="glyphicon glyphicon-log-in" /> Logout
            </a>
          </li>
        </ul>
      </div>
    );
  }
}

export default connect(null, { signoutUser })(Header);

Header.propTypes = {
  signoutUser: PropTypes.func.isRequired,
};
