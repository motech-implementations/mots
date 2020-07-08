import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';

import UserForm from './user-form';
import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { createUser } from '../actions/index';

class UserNew extends Component {
  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    }
  }

  onSubmitCancel() {
    this.props.history.push('/users');
  }

  onSubmit(values) {
    const valuesToSend = values;
    valuesToSend.roles = [{ id: values.roleId }];

    this.props.createUser(valuesToSend, () => {
      Alert.success('New User has been created');
      this.props.history.push('/users');
    });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add User</h1>
        <UserForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} />
      </div>
    );
  }
}

export default connect(null, { createUser })(UserNew);

UserNew.propTypes = {
  createUser: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
