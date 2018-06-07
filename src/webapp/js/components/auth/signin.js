import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';

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
        <span className="input-group-addon"><i className={`glyphicon glyphicon-${icon}`} /></span>
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

      <div className="mainbox col-md-6 col-md-offset-3
        col-sm-8 col-sm-offset-2"
      >
        <div className="row">
          <div className="col-md-8 col-md-offset-2">
            <img className="img-responsive" alt="test" src="/GF_VertLogo_CMYK.jpg" />
          </div>
        </div>

        <div className="row">
          <div className="col-md-8 col-md-offset-2">
            <div className="panel panel-info">
              <div className="panel-heading">
                <div className="panel-title">Sign In</div>
              </div>
              <div className="panel-body padding-top-lg">
                { this.renderAlert() }

                <form className="form-horizontal" onSubmit={handleSubmit(this.onSubmit)}>
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
                    <div className="col-sm-12 controls">
                      <button type="submit" className="btn btn-success">Login</button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
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
