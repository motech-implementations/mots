import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { saveCommunity } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';
import CommunityForm, { COMMUNITY_FORM_NAME } from './community-form';

class CommunityEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      communityValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
    this.fetchCommunity = this.fetchCommunity.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchCommunity();
  }

  onSubmitCancel() {
    this.props.history.push('/locations');
  }

  onSubmit(communityValues) {
    this.setState({ showConfirmModal: true, communityValues });
  }

  onSubmitModal() {
    const valuesToSend = this.state.communityValues;

    this.props.saveCommunity(valuesToSend, () => {
      Alert.success('Community has been updated');
      this.props.history.push('/locations');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchCommunity() {
    const url = `/api/community/${this.props.match.params.communityId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const initialCommunityData = response.data;
          this.props.initialize(COMMUNITY_FORM_NAME, initialCommunityData);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Community</h1>
        <CommunityForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Community?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveCommunity, initialize })(CommunityEdit);

CommunityEdit.propTypes = {
  saveCommunity: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      communityId: PropTypes.string,
    }),
  }).isRequired,
};
