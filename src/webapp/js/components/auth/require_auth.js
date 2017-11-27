import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

export default (ComposedComponent) => {
  class Authentication extends Component {
    componentWillMount() {
      if (!this.props.authenticated) {
        this.props.history.push('/login');
      }
    }

    componentWillUpdate(nextProps) {
      if (!nextProps.authenticated) {
        this.props.history.push('/login');
      }
    }

    render() {
      return <ComposedComponent {...this.props} />;
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
