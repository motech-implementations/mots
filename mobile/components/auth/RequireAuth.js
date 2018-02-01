import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Actions, ActionConst } from 'react-native-router-flux';
import { AsyncStorage } from 'react-native';
import jwtDecode from 'jwt-decode';

import { AUTH_USER, SET_COUNTER_LOGOUT_TIME, UNAUTH_USER } from '../../actions/types';
import { useRefreshToken } from '../../actions';
import { dispatch } from '../../App';

export default (ComposedComponent) => {
  class Authentication extends Component {
    componentWillMount() {
      this.checkIfAuthenticated();
    }

    componentWillUpdate(nextProps) {
      this.checkIfAuthenticated(nextProps);
    }

    checkIfAuthenticated(nextProps) {
      AsyncStorage.getItem('token', (err, token) => {
        if (token) {
          const decoded = jwtDecode(token);
          const currentTime = Date.now() / 1000;
          if (decoded.exp < currentTime) {
            AsyncStorage.getItem('refresh_token', (errRef, refreshToken) => {
              if (refreshToken) {
                const refreshDecoded = jwtDecode(refreshToken);

                if (refreshDecoded.exp < currentTime) {
                  dispatch({ type: UNAUTH_USER });
                } else {
                  dispatch(useRefreshToken(refreshToken));
                }
              } else {
                dispatch({ type: UNAUTH_USER });
              }
            });
          } else {
            dispatch({ type: AUTH_USER });
            dispatch({
              type: SET_COUNTER_LOGOUT_TIME,
              payload: decoded.exp_period,
            });
          }
        } else {
          dispatch({ type: UNAUTH_USER });
        }

        if ((nextProps && !nextProps.authenticated) || !this.props.authenticated) {
          Actions.auth({ type: ActionConst.RESET });
        }
      });
    }

    render() {
      return this.props.authenticated && <ComposedComponent {...this.props} />;
    }
  }

  function mapStateToProps(state) {
    return { authenticated: state.auth.authenticated };
  }

  Authentication.propTypes = {
    authenticated: PropTypes.bool.isRequired,
  };

  return connect(mapStateToProps)(Authentication);
};
