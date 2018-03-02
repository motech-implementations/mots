import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import { hasAuthority, MANAGE_USERS_AUTHORITY } from '../utils/authorization';
import { saveFacility } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';
import FacilityForm, { FACILITY_FORM_NAME } from './facility-form';

class FacilityEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      facilityValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
    this.fetchFacility = this.fetchFacility.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_USERS_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchFacility();
  }

  onSubmitCancel() {
    this.props.history.push('/locations');
  }

  onSubmit(facilityValues) {
    this.setState({ showConfirmModal: true, facilityValues });
  }

  onSubmitModal() {
    const valuesToSend = this.state.facilityValues;

    this.props.saveFacility(valuesToSend, () => {
      Alert.success('Facility has been updated');
      this.props.history.push('/locations');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchFacility() {
    const url = `/api/facility/${this.props.match.params.facilityId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const initialFacilityData = response.data;
          this.props.initialize(FACILITY_FORM_NAME, initialFacilityData);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Facility</h1>
        <FacilityForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Facility?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveFacility, initialize })(FacilityEdit);

FacilityEdit.propTypes = {
  saveFacility: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      facilityId: PropTypes.string,
    }),
  }).isRequired,
};
