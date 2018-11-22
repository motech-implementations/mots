import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Actions, ActionConst } from 'react-native-router-flux';
import { AsyncStorage } from 'react-native';
import jwtDecode from 'jwt-decode';

import { AUTH_USER, UNAUTH_USER } from '../../actions/types';
import { useRefreshToken } from '../../actions';
import { dispatch } from '../../App';

export default (ComposedComponent) => {
  class Authentication extends Component {
    constructor(props) {
      super(props);
      this.state = {
        logoutInterval: null,
      };
    }
    componentWillMount() {
      this.checkIfAuthenticated();
    }
    componentWillUpdate(nextProps) {
      this.checkIfAuthenticated(nextProps);
    }
    componentWillUnmount() {
      clearInterval(this.state.logoutInterval);
    }

    logoutIfExpired = () => {
      const isExpired = this.props.expirationTime < new Date().getTime();
      if (isExpired) {
        dispatch({ type: UNAUTH_USER });
        clearInterval(this.state.logoutInterval);
      }
      return isExpired;
    };

    createLogoutInterval() {
      if (this.state.logoutInterval === null) {
        this.setState({
          logoutInterval: setInterval(this.logoutIfExpired, 1000),
        });
      }
    }

    checkIfAuthenticated(nextProps) {
      AsyncStorage.getItem('token', (err, token) => {
        AsyncStorage.getItem('refresh_token', (errRef, refreshToken) => {
          if (token) {
            const decoded = jwtDecode(token);
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
                dispatch({ type: AUTH_USER });
              }
            } else if (this.props.expirationTime && this.props.authenticated) {
              this.createLogoutInterval();
            }
          } else {
            dispatch({ type: UNAUTH_USER });
          }

          if ((nextProps && !nextProps.authenticated) || !this.props.authenticated) {
            Actions.auth({ type: ActionConst.RESET });
          }
        });
      });
    }

    render() {
      return this.props.authenticated && <ComposedComponent {...this.props} />;
    }
  }

  function mapStateToProps(state) {
    return {
      authenticated: state.auth.authenticated,
      expirationTime: state.auth.expirationTime,
    };
  }

  Authentication.propTypes = {
    authenticated: PropTypes.bool.isRequired,
    expirationTime: PropTypes.number,
  };

  Authentication.defaultProps = {
    expirationTime: null,
  };

  return connect(mapStateToProps)(Authentication);
};
