import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { toast } from 'react-toastify';
import { initialize } from 'redux-form';

import UserProfileForm, { USER_PROFILE_FORM_NAME } from './user-profile-form';
import { saveUserProfile } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';

class UserProfileEdit extends Component {
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

  componentDidMount() {
    this.fetchUser();
  }

  onSubmitCancel() {
    this.props.history.push('/home');
  }

  onSubmit(userValues) {
    this.setState({ showConfirmModal: true, userValues });
  }

  onSubmitModal() {
    const valuesToSend = this.state.userValues;

    this.props.saveUserProfile(valuesToSend, () => {
      toast.success('Your profile has been updated.');
      this.props.history.push('/home');
    });
    this.hideConfirmModal();
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchUser() {
    const url = '/api/user/profile';
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const currentLoggedInUser = response.data;
          this.props.initialize(USER_PROFILE_FORM_NAME, currentLoggedInUser);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Profile</h1>
        <UserProfileForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit your Profile?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveUserProfile, initialize })(UserProfileEdit);

UserProfileEdit.propTypes = {
  saveUserProfile: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
