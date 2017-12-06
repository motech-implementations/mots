import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

const Header = ({ signoutUser, toggleShowMenuSmart }) => (
  <div>
    <div className="navbar-header">
      <button type="button" className="navbar-toggle" toggleMenu="" onClick={toggleShowMenuSmart}>
        <span className="sr-only">Toggle navigation</span>
        <span className="icon-bar" />
        <span className="icon-bar" />
        <span className="icon-bar" />
      </button>
      <Link className="navbar-brand" to="/" onClick={toggleShowMenuSmart}>MOTS</Link>
    </div>
    <ul className="nav navbar-right top-nav">
      <li>
        <Link to="/" onClick={toggleShowMenuSmart}>
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
};
