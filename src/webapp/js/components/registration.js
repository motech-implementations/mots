import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Alert from 'react-s-alert';
import { connect } from 'react-redux';

import { registerUser } from '../actions';
import RegistrationForm from './registration-form';

class Registration extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  onSubmitCancel() {
    this.props.history.push('/');
  }

  onSubmit(values) {
    this.props.registerUser(values, this.props.match.params.token, (result) => {
      if (result.data.id) {
        Alert.success('You have been registered successfully.');
        this.props.history.push('/');
      } else {
        Alert.error('The registration link has expired. Please check your email for a new one.');
        this.props.history.push('/');
      }
    });
  }

  render() {
    return (
      <div>
        <div className="mainbox col-md-6 col-md-offset-3
    col-sm-8 col-sm-offset-2"
        >
          <div className="row">
            <div className="col-md-8 col-md-offset-2">
              <img className="img-responsive" alt="test" src="/EBODAClogo-RGB-with.jpg" />
            </div>
          </div>

          <div className="row">
            <div className="col-md-10 col-md-offset-1">
              <div className="panel panel-info">
                <div className="panel-heading">
                  <div className="panel-title">Sign Up</div>
                </div>
                <div className="panel-body padding-top-lg">
                  <RegistrationForm
                    onSubmit={this.onSubmit}
                    onSubmitCancel={this.onSubmitCancel}
                    token={this.props.match.params.token}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default connect(null, { registerUser })(Registration);

Registration.propTypes = {
  registerUser: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      token: PropTypes.string,
    }),
  }).isRequired,
};
