import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

const Header = ({ signoutUser, toggleShowMenuSmart, hideMenuSmart }) => (
  <div>
    <div className="navbar-header">
      <button type="button" className="navbar-toggle" onClick={toggleShowMenuSmart}>
        <span className="sr-only">Toggle navigation</span>
        <span className="icon-bar" />
        <span className="icon-bar" />
        <span className="icon-bar" />
      </button>
      <Link className="navbar-brand" to="/" onClick={hideMenuSmart}>MOTS</Link>
    </div>
    <ul className="nav navbar-right top-nav hide-max-r-xsmall-max">
      <li>
        <Link to="/">
          <span className="glyphicon glyphicon-user" /> Profile
        </Link>
      </li>
      <li>
        <a href="" onClick={signoutUser}>
          <span className="glyphicon glyphicon-log-in" /> Logout
        </a>
      </li>
    </ul>
  </div>
);


export default Header;

Header.propTypes = {
  signoutUser: PropTypes.func.isRequired,
  toggleShowMenuSmart: PropTypes.func.isRequired,
  hideMenuSmart: PropTypes.func.isRequired,
};
