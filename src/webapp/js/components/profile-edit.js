import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import ProfileForm, { PROFILE_FORM_NAME } from './profile-form';
import { saveUser } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';

class ProfileEdit extends Component {
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
    this.fetchUser();
  }

  onSubmitCancel() {
    this.props.history.push('/');
  }

  onSubmit(userValues) {
    this.setState({ showConfirmModal: true, userValues });
  }

  onSubmitModal() {
    const valuesToSend = this.state.userValues;

    this.props.saveUser(valuesToSend, () => {
      Alert.success('Profile has been updated');
      this.props.history.push('/');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchUser() {
    const url = '/api/user/info';
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const currentLoggedInUser = response.data;
          this.props.initialize(PROFILE_FORM_NAME, currentLoggedInUser);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Profile</h1>
        <ProfileForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Profile?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveUser, initialize })(ProfileEdit);

ProfileEdit.propTypes = {
  saveUser: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
