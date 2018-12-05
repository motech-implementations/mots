import React, { PureComponent } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Actions, ActionConst } from 'react-native-router-flux';
import jwtDecode from 'jwt-decode';

import { AUTH_USER, UNAUTH_USER } from '../../actions/types';
import { useRefreshToken } from '../../actions';
import { dispatch } from '../../App';

export default (ComposedComponent) => {
  class Authentication extends PureComponent {
    constructor(props) {
      super(props);
      this.logoutInterval = null;
    }
    componentWillMount() {
      this.checkIfAuthenticated();
    }
    componentWillUpdate(nextProps) {
      if (nextProps.authenticated !== this.props.authenticated) {
        this.checkIfAuthenticated(nextProps);
      }
    }
    componentWillUnmount() {
      clearInterval(this.logoutInterval);
    }

    logoutIfExpired = () => {
      if (this.props.expirationTime) {
        const isExpired = this.props.expirationTime
            && this.props.expirationTime < new Date().getTime();
        if (isExpired) {
          dispatch({ type: UNAUTH_USER });
          clearInterval(this.logoutInterval);
        }
      } else {
        clearInterval(this.logoutInterval);
      }
    };

    createLogoutInterval() {
      if (this.logoutInterval === null) {
        this.logoutInterval = setInterval(this.logoutIfExpired, 1000);
      }
    }

    checkIfAuthenticated(nextProps) {
      const { accessToken, refreshToken } = this.props;
      if (accessToken) {
        const decoded = jwtDecode(accessToken);
        const currentTime = Date.now() / 1000;
        if (refreshToken) {
          if (decoded.exp < currentTime) {
            const refreshDecoded = jwtDecode(refreshToken);

            if (refreshDecoded.exp < currentTime) {
              dispatch({ type: UNAUTH_USER });
            } else {
              dispatch(useRefreshToken(refreshToken));
            }
          } else {
            dispatch({ type: AUTH_USER, payload: { accessToken, refreshToken } });
          }
        } else if (this.props.expirationTime && this.props.authenticated) {
          this.createLogoutInterval();
        } else {
          dispatch({ type: UNAUTH_USER });
        }
      }

      if ((nextProps && !nextProps.authenticated) || !this.props.authenticated) {
        Actions.auth({ type: ActionConst.RESET });
      }
    }

    render() {
      return this.props.authenticated && <ComposedComponent {...this.props} />;
    }
  }

  function mapStateToProps(state) {
    return {
      authenticated: state.auth.authenticated,
      expirationTime: state.auth.expirationTime,
      accessToken: state.auth.accessToken,
      refreshToken: state.auth.refreshToken,
    };
  }

  Authentication.propTypes = {
    authenticated: PropTypes.bool.isRequired,
    expirationTime: PropTypes.number,
    accessToken: PropTypes.string,
    refreshToken: PropTypes.string,
  };

  Authentication.defaultProps = {
    expirationTime: null,
    accessToken: null,
    refreshToken: null,
  };

  return connect(mapStateToProps)(Authentication);
};
