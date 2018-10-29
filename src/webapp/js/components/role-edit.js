import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import RoleForm, { ROLE_FORM_NAME } from './role-form';
import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { saveRole } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';

class RoleEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      roleValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
    this.fetchRole = this.fetchRole.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchRole();
  }

  onSubmitCancel() {
    this.props.history.push('/roles');
  }

  onSubmit(roleValues) {
    this.setState({ showConfirmModal: true, roleValues });
  }

  onSubmitModal() {
    const valuesToSend = { ...this.state.roleValues };
    valuesToSend.permissions = _.map(this.state.roleValues.permissions, val => ({ id: val.value }));

    this.props.saveRole(valuesToSend, () => {
      Alert.success('Role has been updated');
      this.props.history.push('/roles');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchRole() {
    const url = `/api/role/${this.props.match.params.roleId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const initialUserData = response.data;
          initialUserData.permissions = _.map(initialUserData.permissions, val =>
            ({ value: val.id, label: val.displayName }));
          this.props.initialize(ROLE_FORM_NAME, initialUserData);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Role</h1>
        <RoleForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Role?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveRole, initialize })(RoleEdit);

RoleEdit.propTypes = {
  saveRole: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      roleId: PropTypes.string,
    }),
  }).isRequired,
};
