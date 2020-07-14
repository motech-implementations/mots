import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { toast } from 'react-toastify';

import RoleForm from './role-form';
import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { createRole } from '../actions/index';

class RoleNew extends Component {
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
    this.props.history.push('/roles');
  }

  onSubmit(values) {
    const valuesToSend = { ...values };
    valuesToSend.permissions = _.map(values.permissions, val => ({ id: val }));

    this.props.createRole(valuesToSend, () => {
      toast.success('New role has been created');
      this.props.history.push('/roles');
    });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Add Role</h1>
        <RoleForm onSubmit={this.onSubmit} onSubmitCancel={this.onSubmitCancel} />
      </div>
    );
  }
}

export default connect(null, { createRole })(RoleNew);

RoleNew.propTypes = {
  createRole: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
