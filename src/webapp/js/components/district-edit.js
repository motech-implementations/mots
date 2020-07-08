import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import { hasAuthority, MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY } from '../utils/authorization';
import { saveDistrict } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';
import DistrictForm, { DISTRICT_FORM_NAME } from './district-form';

class DistrictEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      districtValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
    this.fetchDistrict = this.fetchDistrict.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchDistrict();
  }

  onSubmitCancel() {
    this.props.history.push('/locations/3');
  }

  onSubmit(districtValues) {
    this.setState({ showConfirmModal: true, districtValues });
  }

  onSubmitModal() {
    const valuesToSend = {
      id: this.state.districtValues.id,
      name: this.state.districtValues.name,
    };

    this.props.saveDistrict(valuesToSend, () => {
      Alert.success('District has been updated');
      this.props.history.push('/locations/3');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchDistrict() {
    const url = `/api/district/${this.props.match.params.districtId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const initialData = response.data;
          this.props.initialize(DISTRICT_FORM_NAME, initialData);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit District</h1>
        <DistrictForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit District?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveDistrict, initialize })(DistrictEdit);

DistrictEdit.propTypes = {
  saveDistrict: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      districtId: PropTypes.string,
    }),
  }).isRequired,
};
