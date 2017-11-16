import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';

import { signinUser } from '../../actions';

class Signin extends Component {

  renderField(field) {
    const { label, type, input, meta: { touched, error } } = field;
    const className = `form-group ${ touched && error ? 'has-error' : '' }`;

    return (
        <div className={ className }>
          <div className="row">
            <label className="col-md-2 control-label">{ label }</label>
            <div className="col-md-4">
              <input className="form-control" type={ type } { ...input } />
            </div>
          </div>
          <div className="row">
            <div className="col-md-2" />
            <div className="help-block col-md-4">
              { touched ? error : '' }
            </div>
          </div>
        </div>
    );
  }

  onSubmit(values) {
    this.props.signinUser(values, () => {
      this.props.history.push('/');
    });
  }

  renderAlert() {
    if (this.props.errorMessage) {
      return (
          <div className="alert alert-danger">
            <strong>{this.props.errorMessage}</strong>
          </div>
      );
    }
  }

  render() {
    const { handleSubmit } = this.props;

    return (
        <form className="form-horizontal" onSubmit={ handleSubmit(this.onSubmit.bind(this)) }>
          <Field
              label="Username"
              type="text"
              name="username"
              component={ this.renderField }
          />
          <Field
              label="Password"
              type="password"
              name="password"
              component={ this.renderField }
          />
          <div className="row">
            <div className="col-md-2" />
            <div className="col-md-10">
              {this.renderAlert()}
            </div>
          </div>
          <div className="row">
            <div className="col-md-2" />
            <button type="submit" className="btn btn-primary">Login</button>
          </div>
        </form>
    );
  }
}

function validate(values) {
  const errors = {};

  if (!values.username) {
    errors.username = "Enter a username!";
  }

  if (!values.password) {
    errors.password = "Enter a password!";
  }

  return errors;
}

function mapStateToProps(state) {
  return { errorMessage: state.auth.error };
}

export default reduxForm({
  validate,
  form: 'SigninForm'
})(
    connect(mapStateToProps, { signinUser })(Signin)
);
