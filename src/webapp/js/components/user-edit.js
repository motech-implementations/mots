import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import UserForm, { USER_FORM_NAME } from './user-form';
import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { saveUser } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';

class UserEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      userValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
    this.fetchUser = this.fetchUser.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchUser();
  }

  onSubmitCancel() {
    this.props.history.push('/users');
  }

  onSubmit(userValues) {
    this.setState({ showConfirmModal: true, userValues });
  }

  onSubmitModal() {
    const valuesToSend = this.state.userValues;
    valuesToSend.roles = [{ id: valuesToSend.roleId }];

    this.props.saveUser(valuesToSend, () => {
      Alert.success('User has been updated');
      this.props.history.push('/users');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchUser() {
    const url = `/api/user/${this.props.match.params.userId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const initialUserData = response.data;
          initialUserData.roleId = initialUserData.roles[0].id;
          this.props.initialize(USER_FORM_NAME, initialUserData);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit User</h1>
        <UserForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit User?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveUser, initialize })(UserEdit);

UserEdit.propTypes = {
  saveUser: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      userId: PropTypes.string,
    }),
  }).isRequired,
};
