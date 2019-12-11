import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import { hasAuthority, MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY } from '../utils/authorization';
import { saveVillage } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';
import VillageForm, { VILLAGE_FORM_NAME } from './village-form';

class VillageEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      villageValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
    this.fetchVillage = this.fetchVillage.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchVillage();
  }

  onSubmitCancel() {
    this.props.history.push('/locations');
  }

  onSubmit(villageValues) {
    this.setState({ showConfirmModal: true, villageValues });
  }

  onSubmitModal() {
    const valuesToSend = {
      id: this.state.villageValues.id,
      name: this.state.villageValues.name,
      facilityId: this.state.villageValues.facilityId,
    };

    this.props.saveVillage(valuesToSend, () => {
      Alert.success('Village has been updated');
      this.props.history.push('/locations');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchVillage() {
    const url = `/api/village/${this.props.match.params.villageId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const initialVillageData = response.data;
          this.props.initialize(VILLAGE_FORM_NAME, initialVillageData);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Village</h1>
        <VillageForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
          isPasswordRequired={false}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Village?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveVillage, initialize })(VillageEdit);

VillageEdit.propTypes = {
  saveVillage: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      villageId: PropTypes.string,
    }),
  }).isRequired,
};
