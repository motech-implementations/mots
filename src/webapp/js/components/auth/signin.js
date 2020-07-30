import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import FooterInfo from '../footerInfo';

import { signinUser } from '../../actions';

class Signin extends Component {
  constructor(props) {
    super(props);

    this.onSubmit = this.onSubmit.bind(this);
  }

  onSubmit(values) {
    this.props.signinUser(values, () => {
      this.props.history.push('/');
    });
  }

  static renderField(field) {
    const {
      placeholder, type, input, icon, meta: { touched, error },
    } = field;
    const resultingClassName = `input-group margin-bottom-md ${touched && error ? 'has-error' : ''}`;

    return (
      <div className={resultingClassName}>
        <div className="input-group-prepend">
          <span className="input-group-text"><i className={`fa fa-${icon}`} /></span>
        </div>
        <input className="form-control" type={type} placeholder={placeholder} {...input} />
      </div>
    );
  }

  renderAlert() {
    if (this.props.errorMessage) {
      return (
        <div id="login-alert" className="alert alert-danger col-sm-12">
          <strong>{this.props.errorMessage}</strong>
        </div>
      );
    }

    return null;
  }

  render() {
    const { handleSubmit } = this.props;

    return (
      <div className="page-container">
        <div className="login-container col-md-6 offset-md-3 col-sm-8 offset-sm-2">
          <div className="row">
            <div className="col-md-8 offset-md-2">
              <img className="img-fluid" alt="logo" src="/EBODAClogo-RGB-with.jpg" />
            </div>
          </div>

          <div className="row">
            <div className="col-md-8 offset-md-2">
              <div className="card text-info">
                <div className="card-header">
                  <div>Sign In</div>
                </div>
                <div className="card-body">
                  { this.renderAlert() }

                  <form onSubmit={handleSubmit(this.onSubmit)}>
                    <Field
                      placeholder="Username"
                      type="text"
                      name="username"
                      icon="user"
                      component={Signin.renderField}
                      className="form-control"
                    />
                    <Field
                      placeholder="Password"
                      type="password"
                      name="password"
                      icon="lock"
                      component={Signin.renderField}
                      className="form-control"
                    />
                    <div className="form-group">
                      <button type="submit" className="btn btn-success">Login</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div>
          <FooterInfo />
        </div>
      </div>
    );
  }
}

function validate(values) {
  const errors = {};

  if (!values.username) {
    errors.username = 'Enter a username!';
  }

  if (!values.password) {
    errors.password = 'Enter a password!';
  }

  return errors;
}

function mapStateToProps(state) {
  return { errorMessage: state.auth.error };
}

export default reduxForm({
  validate,
  form: 'SigninForm',
})(connect(mapStateToProps, { signinUser })(Signin));

Signin.propTypes = {
  errorMessage: PropTypes.string,
  signinUser: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  handleSubmit: PropTypes.func.isRequired,
};

Signin.defaultProps = {
  errorMessage: null,
};
