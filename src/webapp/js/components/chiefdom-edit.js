import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import { hasAuthority, MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY } from '../utils/authorization';
import { saveChiefdom } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';
import ChiefdomForm, { CHIEFDOM_FORM_NAME } from './chiefdom-form';

class ChiefdomEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      chiefdomValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
    this.fetchChiefdom = this.fetchChiefdom.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchChiefdom();
  }

  onSubmitCancel() {
    this.props.history.push('/locations/2');
  }

  onSubmit(chiefdomValues) {
    this.setState({ showConfirmModal: true, chiefdomValues });
  }

  onSubmitModal() {
    const valuesToSend = {
      id: this.state.chiefdomValues.id,
      name: this.state.chiefdomValues.name,
      districtId: this.state.chiefdomValues.districtId,
    };

    this.props.saveChiefdom(valuesToSend, () => {
      Alert.success('Chiefdom has been updated');
      this.props.history.push('/locations/2');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchChiefdom() {
    const url = `/api/chiefdom/${this.props.match.params.chiefdomId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const initialData = response.data;
          this.props.initialize(CHIEFDOM_FORM_NAME, initialData);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Chiefdom</h1>
        <ChiefdomForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Chiefdom?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveChiefdom, initialize })(ChiefdomEdit);

ChiefdomEdit.propTypes = {
  saveChiefdom: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      chiefdomId: PropTypes.string,
    }),
  }).isRequired,
};
