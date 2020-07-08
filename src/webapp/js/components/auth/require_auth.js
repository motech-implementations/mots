import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import authenticate from './authenticate_token';

export default (ComposedComponent) => {
  class Authentication extends Component {
    componentDidMount() {
      authenticate();
      if (!this.props.authenticated) {
        this.props.history.push('/login');
      }
    }

    componentDidUpdate() {
      authenticate();
      if (!this.props.authenticated) {
        this.props.history.push('/login');
      }
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
    history: PropTypes.shape({
      push: PropTypes.func,
    }).isRequired,
  };

  return connect(mapStateToProps)(Authentication);
};
