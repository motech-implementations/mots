import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Alert from 'react-s-alert';
import { initialize } from 'redux-form';

import 'react-datetime/css/react-datetime.css';

import InchargeForm, { INCHARGE_FORM_NAME } from './incharge-form';
import { saveIncharge } from '../actions';
import apiClient from '../utils/api-client';
import MotsConfirmModal from './mots-confirm-modal';
import { hasAuthority, INCHARGE_WRITE_AUTHORITY } from '../utils/authorization';

class InchargeEdit extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showConfirmModal: false,
      inchargeValues: {},
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onSubmitModal = this.onSubmitModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(INCHARGE_WRITE_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.fetchIncharge();
    }
  }

  onSubmitCancel() {
    this.props.history.push('/incharge');
  }

  onSubmit(inchargeValues) {
    this.setState({ showConfirmModal: true, inchargeValues });
  }

  onSubmitModal() {
    this.props.saveIncharge(this.state.inchargeValues, () => {
      Alert.success('Incharge has been saved');
      this.props.history.push('/incharge');
    });
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  fetchIncharge() {
    const url = `/api/incharge/${this.props.match.params.inchargeId}`;

    apiClient.get(url)
      .then((response) => {
        const incharge = response.data;

        this.props.initialize(INCHARGE_FORM_NAME, incharge);
      });
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Incharge</h1>
        <InchargeForm
          onSubmit={this.onSubmit}
          onSubmitCancel={this.onSubmitCancel}
        />
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="Are you sure to edit Incharge?"
          onConfirm={this.onSubmitModal}
          onHide={this.hideConfirmModal}
        />
      </div>
    );
  }
}

export default connect(null, { saveIncharge, initialize })(InchargeEdit);

InchargeEdit.propTypes = {
  saveIncharge: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  match: PropTypes.shape({
    params: PropTypes.shape({
      inchargeId: PropTypes.string,
    }),
  }).isRequired,
  initialize: PropTypes.func.isRequired,
};
