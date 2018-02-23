import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import CounterLogout from './counter-logout';

const Header = ({ signoutUser, toggleShowMenuSmart, hideMenuSmart }) => (
  <div>
    <div className="navbar-header">
      <button type="button" className="navbar-toggle" onClick={toggleShowMenuSmart}>
        <span className="sr-only">Toggle navigation</span>
        <span className="icon-bar" />
        <span className="icon-bar" />
        <span className="icon-bar" />
      </button>
      <Link className="navbar-brand hide-min-r-small-min" to="/" onClick={hideMenuSmart}>MOTS</Link>
      <Link className="navbar-brand hide-max-r-xsmall-max" to="/" onClick={hideMenuSmart}>Mobile Training and Support</Link>
      <div className="hide-min-r-small-min">
        <CounterLogout />
      </div>
    </div>
    <div className="navbar-right hide-max-r-xsmall-max padding-y-md">
      <CounterLogout />
      <ul className="nav navbar-right top-nav padding-y-md">
        <li>
          <Link to="/">
            <span className="glyphicon glyphicon-user" />
            <span className="icon-text">Profile</span>
          </Link>
        </li>
        <li>
          <a href="" onClick={signoutUser}>
            <span className="glyphicon glyphicon-log-in" />
            <span className="icon-text">Logout</span>
          </a>
        </li>
      </ul>
    </div>
  </div>
);


export default Header;

Header.propTypes = {
  signoutUser: PropTypes.func.isRequired,
  toggleShowMenuSmart: PropTypes.func.isRequired,
  hideMenuSmart: PropTypes.func.isRequired,
};
