import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { toast } from 'react-toastify';
import { initialize } from 'redux-form';

import { hasAuthority, MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY } from '../utils/authorization';
import { saveSector } from '../actions/index';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';
import SectorForm, { SECTOR_FORM_NAME } from './sector-form';

class SectorEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      sectorValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
    this.fetchSector = this.fetchSector.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY)) {
      this.props.history.push('/home');
    }
    this.fetchSector();
  }

  onSubmitCancel() {
    this.props.history.push('/locations/2');
  }

  onSubmit(sectorValues) {
    this.setState({ showConfirmModal: true, sectorValues });
  }

  onSubmitModal() {
    const valuesToSend = {
      id: this.state.sectorValues.id,
      name: this.state.sectorValues.name,
      districtId: this.state.sectorValues.districtId,
    };

    this.props.saveSector(valuesToSend, () => {
      toast.success('Chiefdom has been updated');
      this.props.history.push('/locations/2');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchSector() {
    const url = `/api/sector/${this.props.match.params.sectorId}`;
    apiClient.get(url)
      .then((response) => {
        if (response) {
          const initialData = response.data;
          this.props.initialize(SECTOR_FORM_NAME, initialData);
        }
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Chiefdom</h1>
        <SectorForm
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

export default connect(null, { saveSector, initialize })(SectorEdit);

SectorEdit.propTypes = {
  saveSector: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      sectorId: PropTypes.string,
    }),
  }).isRequired,
};
