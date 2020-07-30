import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

import CounterLogout from './counter-logout';

const Header = ({ signoutUser, toggleShowMenuSmart, hideMenuSmart }) => (
  <div className="w-100">
    <div className="navbar-header">
      <button type="button" className="navbar-toggler" onClick={toggleShowMenuSmart}>
        <span className="navbar-toggler-icon" />
      </button>
      <Link className="navbar-brand hide-min-r-small-min" to="/" onClick={hideMenuSmart}>MOTS</Link>
      <Link className="navbar-brand hide-max-r-xsmall-max" to="/" onClick={hideMenuSmart}>Mobile Training and Support</Link>
    </div>
    <div className="float-right hide-min-r-small-min padding-y-md">
      <CounterLogout />
    </div>
    <div className="float-right hide-max-r-xsmall-max padding-y-md">
      <CounterLogout />
      <ul className="nav float-right top-nav padding-y-md">
        <li className="p-2">
          <Link to="/profile" onClick={hideMenuSmart}>
            <span className="fa fa-user" />
            <span className="icon-text">Profile</span>
          </Link>
        </li>
        <li className="p-2">
          <a href="" onClick={signoutUser}>
            <span className="fa fa-sign-out" />
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
